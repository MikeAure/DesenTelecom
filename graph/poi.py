import geopandas as gpd
import pandas as pd
import requests
from shapely.geometry import Point, LineString
import json
import sys

from sqlalchemy import create_engine, Column, String, Text, Float
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker



pd.set_option('display.max_rows', None)  
pd.set_option('display.max_columns', None)  
pd.set_option('display.width', None)  
pd.set_option('display.max_colwidth', None)



# 定义一个自定义类表示数据表的一行
Base = declarative_base()

class POI(Base):
    __tablename__ = 'poi'

    id = Column(String(10), primary_key=True)
    attribute = Column(String(6), nullable=False)
    name = Column(Text, nullable=False)
    lon = Column(Float, nullable=False)
    lat = Column(Float, nullable=False)

    def __repr__(self):
        return f"POI(id='{self.id}', attribute='{self.attribute}', name='{self.name}', lon={self.lon}, lat={self.lat})"

# 数据库配置
DB_URL = 'mysql+pymysql://root:BRysj0725HhRhL##@localhost:3306/ga?charset=utf8mb4'

def fetch_poi_data():
    # 创建数据库引擎
    engine = create_engine(DB_URL, echo=False)

    # 创建会话
    Session = sessionmaker(bind=engine)
    session = Session()

    try:
        # 查询数据表中的全部数据
        poi_list = session.query(POI).all()
        poi_dict = {poi.id: poi for poi in poi_list}  # 将数据转换为以POI ID为键的字典
        return poi_dict

    except Exception as e:
        print(f"Error fetching data: {e}")
        return {}

    finally:
        session.close()

def extract_ids_from_file(file_path):
    ids_map = {}  
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
        lines = content.splitlines() 
        for line_number, line in enumerate(lines): 
            if line.strip():
                records = line.split(';')
                poi_ids = [] 
                for record in records:
                    if record.strip():  
                        poi_id = record.split(',')[0].strip() 
                        poi_ids.append(poi_id)
                ids_map[line_number + 1] = poi_ids  
    return ids_map


    

def get_poi_details_from_database(poi_id, all_poi_dict):
    return all_poi_dict.get(poi_id)


def get_poi_details(poi_id, api_key):
    url = f"https://restapi.amap.com/v3/place/detail?key={api_key}&id={poi_id}"
    response = requests.get(url)
    
    if response.status_code == 200:
        data = response.json()
        if data["status"] == "1":
            if data["pois"]: 
                poi_info = data["pois"][0]
                poi_details = {
                    "name": poi_info.get("name"),
                    "id": poi_info.get("id"),
                    "address": poi_info.get("address"),
                    "location": poi_info.get("location"), 
                    "telephone": poi_info.get("tel")[0] if poi_info.get("tel") else None, 
                    "type": poi_info.get("type"),
                    "rating": poi_info.get("biz_ext", {}).get("rating"), 
                    "photos": [photo.get("url") for photo in poi_info.get("photos", [])], 
                    "website": poi_info.get("website")[0] if poi_info.get("website") else None, 
                    "city": poi_info.get("cityname"),
                    "adcode": poi_info.get("adcode"),
                    "pname": poi_info.get("pname"),
                    "business_area": poi_info.get("business_area"),
                    "entrance_location": poi_info.get("entr_location"),
                    "indoor_map": poi_info.get("indoor_map"),
                    "type_code": poi_info.get("typecode")
                }
                return poi_details
            else:
                print(f"No POI information found for ID: {poi_id}")
                return None  
        else:
            print(f"Error: {data['info']}")
            return None 
    else:
        print(f"HTTP Error: {response.status_code}")
        return None  
    

def query_poi_details_from_ids(ids_map, all_poi_dict):
    poi_details_map = {}
    
    for line_number, poi_ids in ids_map.items():
        poi_details_list = []
        for poi_id in poi_ids:
            details = get_poi_details_from_database(poi_id, all_poi_dict)
            # time.sleep(1)
            if details:
                poi_details_list.append(details)
        poi_details_map[line_number] = poi_details_list
    
    return poi_details_map


def save_to_shapefile(poi_map, output_file):
    gdf_list = [] 
    
    for line_number, details in poi_map.items():
        for detail in details:
            
            lon, lat = detail.lon, detail.lat
            point = Point(lon, lat)
            # print(f"Point: {point}")
            gdf_list.append({
                'name': detail.name,
                'id': detail.id,
                # 'addr': detail['address'], 
                # 'loc': detail['location'],    
                # 'type': detail['type'],
                # 'rating': detail['rating'],
                # 'city': detail['city'],
                # 'adcode': detail['adcode'],
                # 'pname': detail['pname'],
                # 'b_area': detail['business_area'][:10],  
                'line': line_number,
                'geometry': point,
                'type_code': detail.attribute
            })
            

    gdf = gpd.GeoDataFrame(gdf_list, crs='EPSG:4326') 
    gdf.to_file(output_file, driver='ESRI Shapefile')


def save_to_shapefile_with_lines(poi_map, output_file):
    gdf_list = [] 
    
    for line_number, details in poi_map.items():
        points = [(detail.lon, detail.lat) for detail in details]

        if len(points) > 1:  # 至少需要两个点才能构成线
            line = LineString(points)

            # 将每个 POI 点的信息存入 JSON
            poi_info = [
                {
                    "id": detail.id,
                    "name": detail.name,
                    "lon": detail.lon,
                    "lat": detail.lat,
                    "attribute": detail.attribute
                }
                for detail in details
            ]

            gdf_list.append({
                'line': line_number,
                'geometry': line,
                'POIs': json.dumps(poi_info, ensure_ascii=False)  # 以 JSON 字符串存储
            })

    gdf = gpd.GeoDataFrame(gdf_list, crs='EPSG:4326') 
    gdf.to_file(output_file, driver='ESRI Shapefile')

def read_shapefile(shapefile_path):
    gdf = gpd.read_file(shapefile_path)
    return gdf


def read_shapefile_and_restore_input(shapefile_path, output_file):
    # 读取 Shapefile 文件
    gdf = gpd.read_file(shapefile_path)

    # 恢复输入文件的内容
    restored_lines = {}
    for _, row in gdf.iterrows():
        line_number = row['line']
        poi_id = row['id']
        attribute = row['type_code']

        # 按行组织 POI ID 和属性
        if line_number not in restored_lines:
            restored_lines[line_number] = []
        restored_lines[line_number].append(f"{poi_id},{attribute}")

    # 写回到输出文件
    with open(output_file, 'w', encoding='utf-8') as f:
        for line_number in sorted(restored_lines.keys()):
            f.write(";".join(restored_lines[line_number]) + ";\n")
            

def output_shp_by_row(line_num, line_details, output_file_name):
    gdf_list = []
    for detail in line_details:
        lon, lat = detail.lon, detail.lat
        point = Point(lon, lat)
        # print(f"Point: {point}")
        gdf_list.append({
            'name': detail.name,
            'id': detail.id,
            # 'addr': detail['address'], 
            # 'loc': detail['location'],    
            # 'type': detail['type'],
            # 'rating': detail['rating'],
            # 'city': detail['city'],
            # 'adcode': detail['adcode'],
            # 'pname': detail['pname'],
            # 'b_area': detail['business_area'][:10],  
            'line': line_num,
            'geometry': point,
            'type_code': detail.attribute
        })
            

    gdf = gpd.GeoDataFrame(gdf_list, crs='EPSG:4326') 
    gdf.to_file(output_file_name, driver='ESRI Shapefile')

# KEY = "cf833fb812da8dd3eb2bf5cbcd3b711e"
# KEY = "5f6228a1aa6c4f7f4c45749838887144"


# file_path = 'test.txt' 
# ids_map = extract_ids_from_file(file_path)
# print(ids_map)

# poi_map = query_poi_details_from_ids(ids_map, KEY)
# print(poi_map)

# save_to_shapefile(poi_map, "test.shp")

if __name__ == "__main__":
    txt_path = sys.argv[2]
    shp_path = sys.argv[1]
    print(f"Current: {txt_path}")
    print(f"Output: {shp_path}")
    
    all_poi_dict = fetch_poi_data()


    # for i in range(1, 11):
    # file_path = 'test.txt'
    # output_shp = 'test.shp'

    # print(f"Current: {file_path}")
    
    # ids_map = extract_ids_from_file(file_path)
    # poi_map = query_poi_details_from_ids(ids_map, all_poi_dict)
    # save_to_shapefile(poi_map, output_shp)
    # # save_to_shapefile_with_lines(poi_map, output_shp)
    
    # print(f"Finished: {file_path}")
    # read_shapefile(output_shp)
        # 示例用法
    # shapefile_path = "test.shp"  # 替换为你的 Shapefile 文件路径
    # output_file = f"restored_input_1.txt"  # 输出的恢复文件路径
    # print(f"Generating {output_file}")
    # read_shapefile_and_restore_input(output_shp, output_file)
    # print(f"Generating {output_file} finished")
    
    # 单文件1000行
    file_path = f'../graph_file.txt'
    output_shp = f'../graph_file_shp/graph_file.shp'

    print(f"Current: {txt_path}")
    
    ids_map = extract_ids_from_file(txt_path)
    
    poi_map = query_poi_details_from_ids(ids_map, all_poi_dict)
    save_to_shapefile(poi_map, shp_path)
    print(f"Finished: {txt_path}")
        # 示例用法
    # shapefile_path = "test.shp"  # 替换为你的 Shapefile 文件路径
    # output_file = f"../graph_file_shp/restored_graph_file.txt"  # 输出的恢复文件路径
    # print(f"Generating {output_file}")
    # read_shapefile_and_restore_input(output_shp, output_file)
    # print(f"Generating {output_file} finished")
    
    # print(read_shapefile("./shp/new_graph_file_1.shp"))

import os
from typing import List
import pandas as pd
from sqlalchemy import create_engine, text
from pathlib import Path

# 配置数据库连接信息
DB_USER = 'root'
DB_PASSWORD = '123456QWer!!'
DB_HOST = '127.0.0.1'
DB_PORT = '3306'
DB_NAME = 'ga'

# 创建数据库连接
engine = create_engine(f'mysql+pymysql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}')

# 获取文件夹中的Excel文件列表
folder_path = 'C:\\Users\\Mike\\Desktop\\脱敏项目-文档\\数据库表\\export_excel'
excel_files = [str(f.name) for f in Path(folder_path).glob("*.xlsx")]

print(excel_files)
print(len(excel_files))


def get_table_names():
    result = []
    for excel_file in excel_files:
        base_name = os.path.splitext(excel_file)[0].split("_")[0]

        # 定义表名列表
        table_names = [
            f"{base_name}_user_param",
            f"{base_name}_merchant_param",
            f"{base_name}_service_param"
        ]
        result.extend(table_names)   
    return result


def build_tables():
    # 遍历每个Excel文件并创建相应的数据库表
    for excel_file in excel_files:
        base_name = os.path.splitext(excel_file)[0].split("_")[0]

        # 定义表名列表
        table_names = [
            f"{base_name}_user_param",
            f"{base_name}_merchant_param",
            f"{base_name}_service_param"
        ]
        # 读取Excel文件
        df = pd.read_excel(os.path.join(folder_path, excel_file))
        
        # 创建每个表
        for table_name in table_names:
            # 生成创建表的SQL语句
            create_table_sql = f"""
            CREATE TABLE {table_name} (
                id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                field_name VARCHAR(255) NOT NULL,
                column_name VARCHAR(255) NOT NULL,
                data_type INT NOT NULL,
                k INT NOT NULL,
                tm_param INT NOT NULL
            );
            """
            
            # 执行创建表的SQL语句
            with engine.connect() as connection:
                connection.execute(text(create_table_sql))
                
            df.to_sql(table_name, engine, if_exists='append', index=False)


    print("所有表创建完成！")
    
def change_specific_tm_param(table_names: List[str]):
    
    sql_statements = []

    for table_name in table_names:
        # 根据表名确定 tm_param 的值
        if "user" in table_name:
            tm_param_value = 0
        elif "merchant" in table_name:
            tm_param_value = 2
        elif "service" in table_name:
            tm_param_value = 3
        else:
            print(f"Skipping table {table_name} as it does not match any condition.")
            continue  # 如果表名不符合任何条件，则跳过

        # 构建 SQL 语句
        modify_column_sql = f"UPDATE {table_name} SET tm_param = {tm_param_value};"
        sql_statements.append(modify_column_sql)

    # 执行所有的 SQL 语句
    with engine.connect() as connection:
        with connection.begin():
            for sql in sql_statements:
                print(f"Executing SQL: {sql}")
                try:
                    result = connection.execute(text(sql))
                    print(f"Rows affected: {result.rowcount}")
                except Exception as e:
                    print(f"Error executing SQL: {e}")

    print("全部完成")


if __name__ == "__main__":
    table_names = get_table_names()
    print(table_names)
    print(len(table_names))
    # build_tables()
    change_specific_tm_param(table_names=table_names)
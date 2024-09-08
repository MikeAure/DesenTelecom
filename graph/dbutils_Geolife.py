import os
import pymysql
# 表poi列名
column_name_poi = ("id", "attribute", "name", "lon", "lat", "avg_arv_time", "avg_lev_time", "usr_visitTimes")
table_name_poi = "poi"
# 表start列名
column_name_start = ("poiId", "count")
table_name_start = "start"
# 表end列名
column_name_end = ("attributeId", "prefix", "count")
table_name_end = "end"
# 表category2列名
column_name_category2 = ("nodeId", "prefix", "count")
table_name_category2 = "category2"
# 表category1列名
column_name_category1 = ("nodeId", "prefix", "count")
table_name_category1 = "category1"
# 表category0列名
column_name_category0 = ("nodeId", "prefix", "count")
table_name_category0 = "category0"

dbconn = None

# 连接db
def connect_db():
    try:
        db = pymysql.connect(host="localhost", user="root", passwd="BRysj0725HhRhL##", db="ga")
        
        # db = pymysql.connect(host="localhost", user="root", passwd="123456", db="ga", port=3211)

        return db
    except pymysql.Error as e:
        warnString = "Mysql Error %d: %s" % (e.args[0], e.args[1])
        print(warnString)
        # os._exit(1)
        if (e.args[0] == 1045 or e.args[0] == 1044):
            os._exit(1)


# 关闭db
def close_db(conn=None):
    global dbconn
    if (conn == None):
        conn = dbconn
    if (conn == None):
        return
    cursor = conn.cursor()
    if (cursor):
        cursor.close()
    conn.close()


# 执行sql
def execute_to_db(sql, conn=None):
    global dbconn
    if (conn == None):
        if (dbconn == None):
            conn = connect_db()
        else:
            conn = dbconn

    if (conn):
        try:
            cursor = conn.cursor()
            n = cursor.execute(sql)
            conn.commit()
            print(n, end = " ", flush=True)
            return cursor
        except pymysql.Error as e:
            print(e)
    else:
        return 0


def insert_poi_record(conn, id, attribute, name, lon, lat, avg_arv_time, avg_lev_time, usr_visitTimes):
    sql = "INSERT INTO poi(id, attribute, name, lon, lat, avg_arv_time, avg_lev_time,usr_visitTimes) \
VALUES ('%s', '%s', '%s', '%f','%f', '%d', '%d','%s')" % \
          (id, attribute, name, lon, lat, avg_arv_time, avg_lev_time, usr_visitTimes)
    # print(sql) 
    execute_to_db(sql, conn)


def update_poi_record(conn, poiId, avg_arv_time, avg_lev_time, usr_visitTimes):
    sql = "UPDATE poi SET avg_arv_time = '%d', avg_lev_time = '%d', usr_visitTimes = '%s' WHERE id = '%s'" % \
          (avg_arv_time, avg_lev_time, usr_visitTimes, poiId)
    # print(sql) 
    execute_to_db(sql, conn)


def insert_start_record(conn, poiId, count):
    sql = "INSERT INTO start(poiId, count) VALUES ('%s', '%d')" % (poiId, count)
    # print(sql) 
    execute_to_db(sql, conn)


def update_start_record(conn, poiId, count):
    sql = "UPDATE start SET count = '%d' WHERE poiId = '%s'" % (count, poiId)
    # print(sql) 
    execute_to_db(sql, conn)


def insert_category0_record(conn, nodeId, prefix, count, distance):
    sql = "INSERT INTO category0(nodeId, prefix, count, meanDistance) VALUES ('%s', '%s', '%d','%f')" % (
        nodeId, prefix, count, distance)
    # print(sql) 
    execute_to_db(sql, conn)


def update_category0_record(conn, nodeId, prefix, count, distance):
    sql = "UPDATE category0 SET count = '%d', meanDistance = %f WHERE nodeId = '%s' and prefix = '%s'" % (
        count, distance, nodeId, prefix)
    # print(sql) 
    execute_to_db(sql, conn)


def insert_category1_record(conn, nodeId, prefix, count):
    sql = "INSERT INTO category1(nodeId, prefix, count) VALUES ('%s', '%s', '%d')" % (nodeId, prefix, count)
    # print(sql) 
    execute_to_db(sql, conn)


def update_category1_record(conn, nodeId, prefix, count):
    sql = "UPDATE category1 SET count = '%d' WHERE nodeId = '%s' and prefix = '%s'" % (count, nodeId, prefix)
    # print(sql) 
    execute_to_db(sql, conn)


def insert_category2_record(conn, nodeId, prefix, count):
    sql = "INSERT INTO category2(nodeId, prefix, count) VALUES ('%s', '%s', '%d')" % (nodeId, prefix, count)
    # print(sql) 
    execute_to_db(sql, conn)


def update_category2_record(conn, nodeId, prefix, count):
    sql = "UPDATE category2 SET count = '%d' WHERE nodeId = '%s' and prefix = '%s'" % (count, nodeId, prefix)
    # print(sql) 
    execute_to_db(sql, conn)


def insert_end_record(conn, nodeId, prefix, count):
    sql = "INSERT INTO end(attributeId, prefix, count) VALUES ('%s', '%s', '%d')" % (nodeId, prefix, count)
    # print(sql) 
    execute_to_db(sql, conn)


def update_end_record(conn, nodeId, prefix, count):
    sql = "UPDATE end SET count = '%d' WHERE attributeId = '%s' and prefix = '%s'" % (count, nodeId, prefix)
    # print(sql) 
    execute_to_db(sql, conn)


def query_start_record(conn):
    sql = "SELECT * FROM start"
    # print(sql) 
    return execute_to_db(sql, conn)


def query_end_record(conn):
    sql = "SELECT * FROM end"
    # print(sql) 
    return execute_to_db(sql, conn)


def query_category2_record(conn, prefix):
    sql = "SELECT nodeId,count FROM category2 WHERE prefix =  '%s'" % (prefix)
    # print(sql) 
    return execute_to_db(sql, conn)


def query_category1_record(conn, prefix, father):
    sql = "SELECT nodeId,count FROM category1 WHERE prefix =  '%s' and LEFT(nodeId,2) = '%s'" % (prefix, father)
    # print(sql) 
    return execute_to_db(sql, conn)


def query_category0_record(conn, prefix, father):
    sql = "SELECT nodeId,count FROM category0 WHERE prefix =  '%s' and LEFT(nodeId,4) = '%s'" % (prefix, father)
    # print(sql) 
    return execute_to_db(sql, conn)


def query_all_category0_record(conn):
    sql = "SELECT * FROM category0 "
    # print(sql) 
    return execute_to_db(sql, conn)


def query_poi_record(conn, attribute):
    sql = "SELECT id FROM poi WHERE attribute = '%s' " % (attribute)
    # print(sql) 
    return execute_to_db(sql, conn)


def query_all_poi(conn):
    sql = "SELECT id, attribute, lon, lat FROM poi"
    # print(sql) 
    return execute_to_db(sql, conn)


def query_poi(conn, id):
    sql = "SELECT lon, lat FROM poi where id = '%s' " % (id)
    # print(sql) 
    return execute_to_db(sql, conn)


def query_maxEntropy(conn):
    sql = "select MAX(entropy) from poi"
    # print(sql) 
    return execute_to_db(sql, conn)


def query_all_poi_usr_visitTimes(conn):
    sql = "SELECT id, usr_visitTimes FROM poi"
    # print(sql)
    return execute_to_db(sql, conn)


def update_poi_entropy(conn, poiId, entropy):
    sql = "UPDATE poi SET entropy = '%f' WHERE id = '%s'" % \
          (entropy, poiId)
    # print(sql) 
    execute_to_db(sql, conn)


def update_poi_visitTimes(conn, poiId, visitTimes):
    sql = "UPDATE poi SET total_visit_times = '%d' WHERE id = '%s'" % \
          (visitTimes, poiId)
    # print(sql) 
    execute_to_db(sql, conn)


def insert_meanDistance(conn, start, end, distance):
    sql = "INSERT INTO meanDistance(start, end, meanDistance) VALUES ('%s', '%s', '%f')" % (start, end, distance)
    # print(sql) 
    execute_to_db(sql, conn)


def query_end(conn, prefix, attributeId):
    sql = "SELECT count FROM end WHERE prefix = '%s' and attributeId = '%s'" % (prefix, attributeId)
    # print(sql) 
    return execute_to_db(sql, conn)


def query_meanDistance(conn, start, end):
    sql = "SELECT meanDistance FROM meanDistance WHERE start = '%s' and end = '%s'" % (start, end)
    # print(sql) 
    return execute_to_db(sql, conn)


def query_poi_id(conn):
    sql = "SELECT id FROM poi"
    # print(sql) 
    return execute_to_db(sql, conn)

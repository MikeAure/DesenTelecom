package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ga.LatLong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BasicDataDao {

    @Select("SELECT pay_amount FROM user_info_total where user_index >= #{start} LIMIT #{range}")
    List<Double> selectPayAmountInRange(int start, int range);

    @Results({
        @Result(property = "latitude", column = "current_latitude"),
        @Result(property = "longitude", column = "current_longitude")
    })
    @Select("SELECT current_latitude, current_longitude FROM user_info_total where user_index >= #{start} LIMIT #{range}")
    List<LatLong> selectLatitudeAndLongitudeInRange(int start, int range);
}

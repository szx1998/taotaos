package com.taotao.mapper;


import com.taotao.pojo.TbUser;
import org.apache.ibatis.annotations.Param;

public interface TbUserMapper {
    int checkUserName(@Param("userName") String userName);

    int checkPhone(@Param("phone")String phone);

    int checkEmail(@Param("email") String email);

    int addUser(TbUser tbUser);

    TbUser findUserByUserNameAndPassWord(@Param("userName") String userName,@Param("passWord") String passWord);
}
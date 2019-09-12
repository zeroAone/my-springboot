package com.zeroaone.day01;

import com.zeroaone.day01.bo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Day01ApplicationTests {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void contextLoads() {
    }
    @Test
    public void mysqlTest(){
        String sql="select * from user";
        List<User> users=jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user=new User();
                user.setId(resultSet.getLong("id"));
                return user;
            }
        });
        System.out.println("查询成功");
        for(User user:users){
            System.out.println("id:"+user.getId());
        }
    }

}

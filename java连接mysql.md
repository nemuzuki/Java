java连接数据库

参考：https://blog.csdn.net/baidu_41560343/article/details/86489987

url必须写全了，不然报错，数据库端口为3306（不是apache的）

jdk1.8.0不可以，jdk14可以

```java
package com.company;
import java.sql.*;
public class Main {

    public static void main(String[] args)  throws Exception {
	// write your code here
        //1.加载数据访问驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.连接到数据库
        String url = "jdbc:mysql://localhost:3306/mika_test?" +
                "serverTimezone=Asia/Shanghai&" +
                "characterEncoding=utf-8&" +
                "useUnicode=true&";
        String user= "root";
        String password= "1798";
        Connection conn= DriverManager.getConnection(url,user,password);
        
        //3.构建SQL命令
        Statement state=conn.createStatement();
        String sql="select * from score";
        ResultSet rs = state.executeQuery(sql);
        
        while(rs.next()){
            
            String coursename = rs.getString("coursename");
            String score = rs.getString("score");

            System.out.print(coursename);
            System.out.print(score);
        }
        // 完成后关闭
        rs.close();
        state.close();
        conn.close();
    }
}
output:大数据计算及应用99.0
```


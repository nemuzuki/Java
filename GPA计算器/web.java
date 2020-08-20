package sample;
import java.sql.*;
import java.util.ArrayList;

public class web {

    public static ArrayList<Main.Score> get_score_list()throws Exception {
        //1.加载数据访问驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.连接到数据库
        String url = "jdbc:mysql://localhost:3306/mika_test?" +
                "serverTimezone=Asia/Shanghai&" +
                "characterEncoding=utf-8&" +
                "useUnicode=true";
        String user= "root";
        String passwd= "1798";
        Connection conn= DriverManager.getConnection(url,user,passwd);
        //3.构建SQL命令
        Statement state=conn.createStatement();
        String sql="select * from score";
        ResultSet rs = state.executeQuery(sql);


        ArrayList score_list=new ArrayList();
        // 展开结果集数据库
        while(rs.next()){
            // 通过字段检索
            String courseName = rs.getString("courseName");
            String courseId=rs.getString("courseId");
            String score = rs.getString("score");
            String credit=rs.getString("credit");
            Main.Score new_score=new Main.Score(courseId,courseName,credit,score);
            score_list.add(new_score);

        }
        // 完成后关闭
        rs.close();
        state.close();
        conn.close();

        for(int i=0;i<score_list.size();++i){
            Main.printScore((Main.Score)score_list.get(i));
        }
        return score_list;
    }
}

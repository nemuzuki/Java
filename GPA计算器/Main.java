package sample;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.util.ArrayList;

public class Main extends Application {


    private final TableView <Score>table = new TableView<>();

    //自定义Score类
    public static class Score{
        private final SimpleStringProperty courseId;
        private final SimpleStringProperty courseName;
        private final SimpleStringProperty credit;
        private final SimpleStringProperty score;
        public Score(String courseid,String coursename,String credit,String score){//输入的是普通string，转成Simple
            this.courseId=new SimpleStringProperty(courseid);
            this.courseName=new SimpleStringProperty(coursename);
            this.credit=new SimpleStringProperty(credit);
            this.score=new SimpleStringProperty(score);
        }

        public String getCourseId(){
            return courseId.get();
        }

        public String getCourseName(){
            return courseName.get();
        }
        public String getCredit(){
            return credit.get();
        }
        public String getScore(){
            return score.get();
        }
        public void setCourseId(String fCourseId){
            courseId.set(fCourseId);
        }
        public void setCourseName(String fCourseName){
            courseName.set(fCourseName);
        }
        public void setCredit(String fCredit){
            credit.set(fCredit);
        }
        public void setScore(String fScore){
            score.set(fScore);
        }
    }

    public static void printScore(Score score){
        System.out.println(score.courseId+" "+score.courseName+" "+score.credit+" "+score.score);
    }

    public static float weight_mean(ArrayList<Score> score_list){
        float sum=0;
        float sum_credit=0;
        for(int i=0;i<score_list.size();++i){
            Score s=score_list.get(i);
            String score_str=s.score.getValue();        //simpleStr->str
            float score=Float.parseFloat(score_str);    //str->float
            String credit_str=s.credit.getValue();
            float credit=Float.parseFloat(credit_str);

            sum+=credit*score;
            sum_credit+=credit;
        }
        float avg=sum/sum_credit;
        return avg;
    }
    public static float standardGPA(ArrayList<Score> score_list){
        float sum=0;
        float sum_credit=0;
        for(int i=0;i<score_list.size();++i){
            Score s=score_list.get(i);
            String score_str=s.score.getValue();        //simpleStr->str
            float score=Float.parseFloat(score_str);    //str->float
            String credit_str=s.credit.getValue();
            float credit=Float.parseFloat(credit_str);

            if(score>=90)sum+=credit*4.0;
            else if(score>=80)sum+=credit*3.0;
            else if(score>=70)sum+=credit*2.0;
            else if(score>=60)sum+=credit;
            sum_credit+=credit;
        }
        float avg=sum/sum_credit;
        return avg;
    }
    public static float peking(ArrayList<Score> score_list){
        float sum=0;
        float sum_credit=0;
        for(int i=0;i<score_list.size();++i){
            Score s=score_list.get(i);
            String score_str=s.score.getValue();        //simpleStr->str
            float score=Float.parseFloat(score_str);    //str->float
            String credit_str=s.credit.getValue();
            float credit=Float.parseFloat(credit_str);

            if(score>=90)sum+=credit*4.0;
            else if(score>=85)sum+=credit*3.7;
            else if(score>=82)sum+=credit*3.3;
            else if(score>=78)sum+=credit*3.0;
            else if(score>=75)sum+=credit*2.7;
            else if(score>=72)sum+=credit*2.3;
            else if(score>=68)sum+=credit*2.0;
            else if(score>=64)sum+=credit*1.5;
            else if(score>=60)sum+=credit;
            sum_credit+=credit;
        }
        float avg=sum/sum_credit;
        return avg;
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Check ur GPA");
        primaryStage.getIcons().add(new Image("file:C:\\Users\\Mika\\Desktop\\oshio.jpg")); //程序的小图标
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);

        //表头
        TableColumn courseId = new TableColumn("courseId");//text对应的是Score类里面属性的名字，所以大小写必须一致
        courseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        TableColumn courseName = new TableColumn("courseName");
        courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        TableColumn credit = new TableColumn("credit");
        credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        TableColumn score = new TableColumn("score");
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        courseId.setMinWidth(200);  //表头宽度
        courseName.setMinWidth(350);
        credit.setMinWidth(200);
        score.setMinWidth(200);

        //填表
        table.setEditable(true);
        ArrayList <Score> score_list=web.get_score_list();
        ObservableList <Score> observableArrayList = FXCollections.observableArrayList(score_list);//从数据库获取表
        final ObservableList<Score> data = FXCollections.observableArrayList(observableArrayList);//ArrayList转OAL
        table.setItems(data);
        table.getColumns().addAll(courseId,courseName,credit,score);

        Label lbl_weight=new Label("加权平均：");
        float weight_mean=weight_mean(score_list);
        Label lbl_weight_mean=new Label(String.valueOf(weight_mean));

        Label lbl_standard=new Label("标准算法：");
        float standardGPA=standardGPA(score_list);
        Label lbl_standard_gpa=new Label(String.valueOf(standardGPA));

        Label lbl_peking=new Label("北大算法：");
        float pekingGPA=peking(score_list);
        Label lbl_peking_gpa=new Label(String.valueOf(pekingGPA));

        //布局
        final VBox vbox = new VBox();
        vbox.setSpacing(5);//各个子组件间的距离
        vbox.setPadding(new Insets(10, 0, 0, 10));//组件与vbox边缘距离
        vbox.getChildren().addAll(table,lbl_weight,lbl_weight_mean,lbl_standard,lbl_standard_gpa,lbl_peking,lbl_peking_gpa);


        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args)throws Exception {
        launch(args);
    }
}

### 前期配置
```xml
首先去下载jdk1.8.0和Oracle的JavaFX Scene Builder 2.0
在IDEA里面project structure设置SDKs，加入jdk的总文件夹路径
然后project SDK也改成1.8的
在settings里面的Languages&Frameworks选javafx，确定JavaFX Scene Builder 2.0中那个exe的路径
然后创建new javafx project
最后在主界面的run三角左边，edit configuration，JRE改成1.8
終わり
```
然后就得到了Main.java
```java
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
```
一个字数统计的例子，用到了一个TextArea（多行文本框），一个button，一个显示字数的rectangle
```java
package sample;
import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ContentDisplay;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 800));

        TextArea taNote=new TextArea("Hello World");
        taNote.setPrefColumnCount(20);
        taNote.setPrefRowCount(5);
        taNote.setWrapText(true);



        Rectangle rectangle=new Rectangle(10,10,200,120);
        rectangle.setStroke(Color.BLUE);    //边框颜色
        rectangle.setFill(Color.rgb(39,197,187));     //内部颜色
        StackPane stackPane=new StackPane();
        Label lbl_in_rec=new Label();   //内部文字
        lbl_in_rec.setTextFill(Color.PURPLE);
        lbl_in_rec.setFont(new Font("Consolas",22));
        stackPane.getChildren().addAll(rectangle,lbl_in_rec);   //图形内部文字
        Label lbl_date=new Label("below",stackPane);  //标签
        lbl_date.setContentDisplay(ContentDisplay.TOP);   //top/bottom/right/left表示图形相对标签的位置
        lbl_date.setTextFill(Color.RED);  //标签文字颜色

        Button btn=new Button("count");//字数统计
        btn.setOnAction(e -> {
            String txt=taNote.getText();
            lbl_in_rec.setText(""+txt.length());
        });

        HBox pane=new HBox(20);
        pane.getChildren().addAll(taNote,lbl_date,btn);//将所有部件整合上


        primaryStage.setScene(new Scene(pane,1000,800));
        primaryStage.getIcons().add(new Image("file:C:\\Users\\Mika\\Desktop\\oshio.jpg")); //程序的小图标
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

```

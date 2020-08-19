### 前期配置

> 1.下载jdk1.8.0（必须用这个版本）和Oracle的JavaFX Scene Builder 2.0
>
> 2.在IDEA里面project structure设置SDKs，加入jdk的总文件夹路径
>
> 3.然后project SDK也改成1.8的
>
> 4.在settings里面的Languages&Frameworks选javafx，确定JavaFX Scene Builder 2.0中那个exe的路径
>
> 5.创建new javafx project
> 6.最后在主界面的run三角左边，edit configuration，JRE改成1.8
> 終わり

然后就得到了Main.java，Controller.java和sample.fxml三个文件，main是主函数，controller和fxml由SceneBuilder手动布局后生成

#### JavaFX可以使用两种方法构造gui：

> 1.一种是直接在main里面写代码，通过HBox生成一个窗格pane
>
> 2.另一种是通过fxml来生成，右键fxml文件，Open in SceneBuilder，手动设计后，View->Show Sample Controller Skeleton把代码复制到controller里面去。这种方法需要构造fxml到main的root：
>
> ```java
> Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
> primaryStage.setScene(new Scene(root, 1000, 800));
> ```



一个字数统计的例子（Main.java），用到了一个TextArea（多行文本框），一个button，一个显示字数的rectangle：

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
        primaryStage.setTitle("Hello World");

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

通过fxml手动布局：

```java
//Main.java（忽略库）
public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

//Controller.java
package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    @FXML
    private Button okayButton;

    @FXML
    void okay(ActionEvent event) {
        System.out.println("ok");
    }

}
```

```xml
//fxml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.shape.*?>
<?import java.lang.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.layout.GridPane?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>


<GridPane alignment="center" hgap="10" prefHeight="509.0" prefWidth="468.0" vgap="10" xmlns:fx="http://javafx.com/fxml/1" xmlns="http://javafx.com/javafx/8" fx:controller="sample.Controller">
   <columnConstraints>
      <ColumnConstraints />
      <ColumnConstraints />
      <ColumnConstraints />
      <ColumnConstraints />
   </columnConstraints>
   <rowConstraints>
      <RowConstraints />
      <RowConstraints />
      <RowConstraints />
      <RowConstraints />
   </rowConstraints>
   <children>
      <Button fx:id="okayButton" mnemonicParsing="false" onAction="#okay" text="ok" GridPane.columnIndex="1" />
   </children>
</GridPane>

```


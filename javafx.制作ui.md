### 前期配置
```xml
首先去下载jdk1.8.0和Oracle的JavaFX Scene Builder 2.0
在IDEA里面project structure设置SDKs，加入jdk的总文件夹路径
然后project SDK也改成1.8的
在settings里面的Language&Frameworks选javafx，确定JavaFX Scene Builder 2.0中那个exe的路径
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

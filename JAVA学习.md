# JAVA

输出：System.out.println();​

输入：

通过import java.util.Scanner导入Scanner类

创建输入对象：Scanner input=new Scanner(System.in);

赋值：double r=input.nextDouble();

```java
import java.util.Scanner;
class test{	//如果用public class，总类名要与文件名相同（test.java）
    public static void main(String[] args){
        Scanner input=new Scanner(System.in);
        double r=input.nextDouble();
        System.out.println("the area of the circle="+3.14*r*r);
    }
}

the area of the circle=12.56
```

import java.util.* 能够导入util包里面所有的类

常量final：final double pi=3.14；

| 数值类型 | 大小  |
| -------- | ----- |
| byte     | 8bit  |
| short    | 16bit |
| int      | 32bit |
| long     | 64bit |
| float    | 32bit |
| double   | 64bit |

幂运算：Math.pow(底数，指数)

大数输出时必须在后面加L，默认int->long，System.out.println(21474_83648L);

支持在数字之间加下划线提高可读性

类型转换：b=(double)a;

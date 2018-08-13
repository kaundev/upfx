<h1>UpFX</h1>

I created this tool to help in developement using JavaFX allowing CDI and other features.

How to use it:
- Using Intellij, you can create a new JavaFX project;
- Then you can add maven support;
- In the resources folder, add the folder META-INF, and inside it, the beans.xml file. This will allow CDI.
- Put the .fxml files inside resources folder. Personally I create views folder and put inside it.
- In pom.xml you can import via dependency.

<h2>First Steps:<h2>

<h3>Creating the Main Class:</h3>

- First extends FxMain in your Main class;
- Import Result from com.upfx.result.Result via injection;
- Call initiate method passing the Main class
- With result you can call render method with the fxml location and the title

```java
public class Main extends FxMain {

    @Inject
    private Result result;

    public static void main(String[] args) {
        initiate(Main.class, args);
    }

    @Override
    protected void firstScreen() {
        result.render("/views/sample.fxml", "My first ice screen");
    }
}
```
  <h3>Controllers</h3>

- Just extends Controller from com.upfx.controller.Controller;
- There is no need to implement the initialize JavaFX method, the Controller super class already does that.
Location and resouceBundle will be accessed via instance field;
- Instead of "initialize" method, this tool offers another method: initiate (not very creative, sorry).
This method can receive parameters and will be called when you use result to navigate;


```java
public class MainController extends Controller {

    public void initiate(){
        ResourceBundle resourceBundle = this.resourceBundle;
        URL location = this.location;
    }
}
```
<h3>Navigation</h3>

- result method from the example above receives a string url of .fxml file, string title of the page
and others parameters that you can pass through controllers. See the example:

```java
String path = "/views/show_message.fxml"
String title = "Show parameters"
String parameter1 = "This is a parameter being passed"
String yetAnotherParameter = "Yes, I can receive as much parameters your controller needs"

result.render(path, title, parameter1, yetAnotherParameter);
```

- In the controller being called you can do

```java
public void initiate(String parameter1, String yetAnotherParameter){
    System.out.println(parameter1);
    System.out.println(yetAnotherParameter);
}
```

<h4>getPane</h4>
The render method will render another screen in your project, but if you want to change a specific element of your screen you can call "getPane".
getPane doesn't receives the title, but you can pass as much parameters you need. getPane also returns the controller that you are calling. Use getRoot() method to render in the element.

```java
PaneController paneController = result.getPane("/views/lessons/studentCard.fxml", "A parameter");
this.changeblePane.getChildren().add(paneController.getRoot());
```

For code organization, I recomend creating a Routes class and putting all the routes inside it.

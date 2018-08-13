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


```xml
<dependency>
    <groupId>com.github.kaundev.upfx</groupId>
    <artifactId>upfx-core</artifactId>
    <version>1.0.2</version>
</dependency>
```

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

<h4>Rendering</h4>
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

<h3>Popup</h3>
Also you can create popups, passing .fxml location, the title and other parameters you need.

```java
public void popupNoPermission(){
    result.openPopup("/views/users/noPermission.fxml", "No permission");
}
```

For code organization, I recomend creating a Routes class and putting all the routes inside it.

<h2>Security</h2>

- First create an anotation that represents the existing rules. In this example SecurityType is an Enum.

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityTypeRule {
    SecurityType type();
}
```

- Then, create an Handler if the user doenst have permission:

```java
public class AuthenticationSecurityHandler extends SecurityHandler{

    private Routes routes;

    @Inject
    public AuthenticationSecurityHandler(Routes routes) {
        this.routes = routes;
    }

    @Override
    public void handle(InvocationContext invocationContext) {
        routes.popupNoPermission();
    }
}
```

- Then, create the Rule class implemeting SecurityRule;
- Annotate the class with @Handler passing the handler you created
- To perform the security create "isAllowed" method return a boolean. This method receives an AnnotatedElement that can be a class or an method. From annotatedElement you can retreive the annotation created.

```java
@Handler(AuthenticationSecurityHandler.class)
public class DefaultRule implements SecurityRule {

    private LoggedUser loggedUser;

    @Inject
    public DefaultRule(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    public boolean isAllowed(AnnotatedElement annotatedElement) {
        SecurityTypeRule securityTypeRule = annotatedElement.getAnnotation(SecurityTypeRule.class);
        return loggedUserHasPermission(securityTypeRule.type)
    }
}
```

- For Controller classes, just call Class security Annotation passing the ClassRule
```java
@ClassSecurity(value = DefaultRule.class) @SecurityTypeRule(type =  SecurityType.list_student)
public class MySecurityController extends Controller{
    ...
}
```

- For methods, call MethodSecurity annotation passing the ClassRule
```java
@FXML @MethodSecurity(value = DefaultRule.class) @SecurityTypeRule(type = SecurityType.create_admin)
protected void addAdmin(){
    routes.newAdministrator();
}
```

<h2>Multi language from ResourceBundle</h2>
- The only thing to do is create a producer method passing the location of the properties file, like: "Bundle_pt_BR.properties" inside META-INF/locales/Bundle_pt_BR.properties

```java
public class ResourceBundleProducer {

    @Produces
    public ResourceBundle produceResourceBundle() {
        return ResourceBundle.getBundle("META-INF/Bundle", new Locale("pt", "BR"));
    }
}
```

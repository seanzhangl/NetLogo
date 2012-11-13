package org.nlogo.modelingcommons;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.nlogo.app.ModelSaver;

import javax.swing.JDialog;
import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ben
 * Date: 10/25/12
 * Time: 1:19 AM
 * To change this template use File | Settings | File Templates.
 */



public class ModelingCommons {
  private static final String HOST = "http://localhost:3000";
  //private static final String HOST = "http://modelingcommons.org";
  private static HttpClient http = new DefaultHttpClient();
  private JSONParser json = new JSONParser();
  private static Person person = null;
  private String uploadedModelURL;
  private String uploadedModelName;
  private ModelSaver modelSaver;
  private Frame frame;

  public ModelingCommons(ModelSaver modelSaver, Frame frame) {
    this.modelSaver = modelSaver;
    this.frame = frame;
  }
  public class Person {
    private String firstName;
    private String lastName;
    private int id;
    private String avatarURL;
    private String emailAddress;


    public Person(String firstName, String lastName, int id, String avatarURL, String emailAddress) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.id = id;
      this.avatarURL = avatarURL;
      this.emailAddress = emailAddress;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public int getId() {
      return id;
    }

    public String getAvatarURL() {
      return avatarURL;
    }

    public String getEmailAddress() {
      return emailAddress;
    }
  }
  String login(String email, String password) {
    System.out.println("Logging in");
    try {

      HttpPost post = new HttpPost(HOST + "/account/login_action");
      post.addHeader("Accept", "application/json");
      List<NameValuePair> credentials = new ArrayList<NameValuePair>(2);
      credentials.add(new BasicNameValuePair("email_address", email));
      credentials.add(new BasicNameValuePair("password", password));
      UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(credentials, Consts.UTF_8);
      post.setEntity(formEntity);
      HttpResponse response =  http.execute(post);
      HttpEntity entity = response.getEntity();
      String responseStr = EntityUtils.toString(response.getEntity());
      System.out.println(responseStr);
      EntityUtils.consume(entity);
      try {
        JSONObject obj = (JSONObject)(json.parse(responseStr));
        String status = (String)(obj.get("status"));
        System.out.println("Status: " + status);
        if(status.equals("SUCCESS")) {
          JSONObject personObj = (JSONObject)(obj.get("person"));
          person = new Person(
              (String)(personObj.get("first_name")),
              (String)(personObj.get("last_name")),
              ((Number)(personObj.get("id"))).intValue(),
              (String)(personObj.get("avatar")),
              (String)(personObj.get("email_address"))
          );
        }

        return status;
      } catch(ParseException e) {
        return "INVALID_RESPONSE_FROM_SERVER";
      }


    } catch(IOException e) {
      return "CONNECTION_ERROR";
    }
  }
  String logout() {
    System.out.println("Logging out");
    try {
      HttpPost post = new HttpPost(HOST + "/account/logout");
      post.addHeader("Accept", "application/json");

      HttpResponse response =  http.execute(post);
      HttpEntity entity = response.getEntity();
      String responseStr = EntityUtils.toString(response.getEntity());
      EntityUtils.consume(entity);
      try {
        JSONObject obj = (JSONObject)(json.parse(responseStr));
        String status = (String)(obj.get("status"));
        System.out.println("Status: " + status);
        if(status.equals("SUCCESS") || status.equals("NOT_LOGGED_IN")) {
          person = null;
        }
        return status;
      } catch(ParseException e) {
        return "INVALID_RESPONSE_FROM_SERVER";
      }


    } catch(IOException e) {
      return "CONNECTION_ERROR";
    }
  }

  String uploadModel(final String modelName) {
    System.out.println("uploading model");
    try {

      HttpPost post = new HttpPost(HOST + "/upload/create_model");
      post.addHeader("Accept", "application/json");

      MultipartEntity requestEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
      requestEntity.addPart("new_model[name]", new StringBody(modelName, "text/plain", Consts.UTF_8));
      requestEntity.addPart("new_model[uploaded_body]", new StringBody(modelSaver.save(), "text/plain", Consts.UTF_8) {
        public String getFilename() {
          return modelName + ".nlogo";
        }
      });
      post.setEntity(requestEntity);

      HttpResponse response =  http.execute(post);
      HttpEntity entity = response.getEntity();
      String responseStr = EntityUtils.toString(response.getEntity());
      System.out.println(responseStr);
      EntityUtils.consume(entity);
      try {
        JSONObject obj = (JSONObject)(json.parse(responseStr));
        String status = (String)(obj.get("status"));
        System.out.println("Status: " + status);
        if(status.equals("SUCCESS")) {
          JSONObject model = ((JSONObject)(obj.get("model")));
          this.uploadedModelURL = (String)(model.get("url"));
          this.uploadedModelName = (String)(model.get("name"));
        }
        return status;
      } catch(ParseException e) {
        return "INVALID_RESPONSE_FROM_SERVER";
      }


    } catch(IOException e) {
      return "CONNECTION_ERROR";
    }
  }
  public boolean isLoggedIn() {
    return person != null;
  }

  public String getUploadedModelURL() {
    return uploadedModelURL;
  }

  public String getUploadedModelName() {
    return uploadedModelName;
  }

  public Person getPerson() {
    return person;
  }

  public void promptForLogin(String error) {
    JDialog loginDialog = new ModelingCommonsLoginDialog(frame, this, error);
    loginDialog.setVisible(true);
  }
  public void promptForLogin() {
    promptForLogin(" ");
  }
  public void promptForUpload(String error) {
    JDialog uploadDialog = new ModelingCommonsUploadDialog(frame, this, error);
    uploadDialog.setVisible(true);
  }
  public void promptForUpload() {
    promptForUpload(" ");
  }
  public void promptForSuccess(String error) {
    JDialog successDialog = new ModelingCommonsUploadSuccessDialog(frame, this, error);
    successDialog.setVisible(true);
  }
  public void promptForSuccess() {
    promptForSuccess(" ");
  }
  public void saveToModelingCommons() {
    if(!isLoggedIn()) {
      promptForLogin();
    } else {
      promptForUpload();
    }

  }
}

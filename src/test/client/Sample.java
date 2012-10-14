package test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import java.util.ArrayList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.CellTable;


public class Sample implements EntryPoint 
{
   private static class MyWorker
   {
      private final String name;
      private final String username;
      private final String department;
      private final int id;
      
      public MyWorker(String nameStr, String user, String dept, int id)
      {
         name = nameStr;
         username = user;
         department = dept;
         this.id = id;
      }
   }
   ArrayList<MyWorker> workers = new ArrayList<MyWorker>();
   JsArray<Worker> jsonData;
   VerticalPanel mainPanel = new VerticalPanel();
   public void onModuleLoad()
   {
      RootPanel.get().add(mainPanel);
      String url = "http://localhost:3000/workers.json";
      getRequest(url);
   }
   private void getRequest(String url)
   {
      final RequestBuilder rb =
         new RequestBuilder(RequestBuilder.GET,url);
      try {
         rb.sendRequest(null, new RequestCallback()
         {
            public void onError(final Request request,
               final Throwable exception)
            {
               Window.alert(exception.getMessage());
            }
            public void onResponseReceived(final Request request,
               final Response response)
            {
               String text = response.getText();
               showWorkersCellTable(text);
            }
         });
      }
      catch (final Exception e) {
         Window.alert(e.getMessage());
      }
   }
   private JsArray<Worker> getJSONData(String json)
   {
      return JsonUtils.safeEval(json);
   }
   private void showWorkersCellTable(String json)
   {
      jsonData = getJSONData(json);
      Worker worker = null;
      for (int i = 1; i < jsonData.length(); i++) {
         worker = jsonData.get(i);
         String name = worker.getName();
         String username = worker.getUsername();
         String department = worker.getDepartment();
         int id = worker.getId();
         MyWorker w = new MyWorker(name,username,department,id);
         workers.add(w);
      }
      TextColumn<MyWorker> nameCol =
         new TextColumn<MyWorker>()
         {
            @Override
            public String getValue(MyWorker worker)
            {
               return worker.name;
            }
         };
      TextColumn<MyWorker> usernameCol =
         new TextColumn<MyWorker>()
         {
            @Override
            public String getValue(MyWorker worker)
            {
               return worker.username;
            }
         };
      TextColumn<MyWorker> deptCol =
         new TextColumn<MyWorker>()
         {
            @Override
            public String getValue(MyWorker worker)
            {
               return worker.department;
            }
         };
      CellTable<MyWorker> table =
         new CellTable<MyWorker>();
      table.addColumn(nameCol,"Name");
      table.addColumn(usernameCol,"Username");
      table.addColumn(deptCol,"Department");
      table.setRowCount(workers.size(),true);
      table.setRowData(0,workers);
      mainPanel.add(table);
   }
}
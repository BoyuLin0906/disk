package com.example.boyu.disk;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText year , month , day , hour;
    RadioGroup yearTpye , sexType;
    Button Send;
    String s_yearTpye,s_sexTpye;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onClickButton();
    }

    public void onClickButton(){

        Send = (Button)findViewById(R.id.button);
        yearTpye = (RadioGroup)findViewById(R.id.radioGroup);
        sexType = (RadioGroup)findViewById(R.id.radioGroup1);


        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 找性別ID，經過ID判斷是男是女
                int select_year_type = yearTpye.getCheckedRadioButtonId();
                int select_sex_tpye = sexType.getCheckedRadioButtonId();

                if (select_year_type == 2131492945) {
                    s_yearTpye = "0";
                } else if (select_year_type == 2131492946) {
                    s_yearTpye = "1";
                } else {
                    s_yearTpye = "2";
                }

                if (select_sex_tpye == 2131492955) {
                    s_sexTpye = "0";
                } else {
                    s_sexTpye = "1";
                }

                year = (EditText) findViewById(R.id.editYear);
                String s_year = year.getText().toString();
                month = (EditText) findViewById(R.id.editMonth);
                String s_month = month.getText().toString();
                day = (EditText) findViewById(R.id.editDay);
                String s_day = day.getText().toString();
                hour = (EditText) findViewById(R.id.editHour);
                String s_hour = hour.getText().toString();

                String url = Catch_say88_API_info(s_yearTpye, s_year, s_month, s_day, s_hour, s_sexTpye);
                RequestTask request = new RequestTask();
                request.execute(url);
            }
        });

    }

    public String Catch_say88_API_info(String birthType,String Year,String Month , String Day ,String Hour,String Sex){
        // token就是識別證
        String url = "http://newtest.88say.com/Api/product/Unit306.aspx?";
        url += "token=D5DF5A998BF46E8D37E3D600C022D8B0D76D68BABCF7CFC75304E8EF5168A48B";
        url += "&birthType=";
        url += birthType;
        url += "&year=";
        url += Year;
        url += "&month=";
        url += Month;
        url += "&day=";
        url += Day;
        url += "&hour=";
        url += Hour;
        url += "&sex=";
        url += Sex;
        return url;
    }

    public void JsonTrasnfer_output(String jsonInput){
        String TxnCode="";
        String TxnMsg="";
        String Reslut_Sex="";
        String Reslut_Age="";
        String Reslut_Birth="";
        String Reslut_LunarBirth="";
        String Reslut_FateManner="";
        String Reslut_FateSex="";
        String Reslut_Houses_Name="";
        String Reslut_Houses_Decade="";
        String Reslut_Houses_GanZhi="";
        String[] Reslut_Houses_Stars_Name=new String[4];
        String[] Reslut_Houses_Stars_Flag=new String[4];

        try {
            TxnCode = new JSONObject(jsonInput).getString("TxnCode");
            TxnMsg = new JSONObject(jsonInput).getString("TxnMsg");
            Reslut_Sex =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Sex");
            Reslut_Age =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Age");
            Reslut_Birth =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Birth");
            Reslut_LunarBirth =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("LunarBirth");
            Reslut_FateManner =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("FateManner");
            Reslut_FateSex =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("FateSex");
            Reslut_Houses_Name =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Name");
            Reslut_Houses_Decade  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Decade");
            Reslut_Houses_GanZhi  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("GanZhi");
            count = (new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses"))).length()/4;

            for (int i=0;i< count ;i++){
               Reslut_Houses_Stars_Name[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Stars")).getJSONObject(i).getString("Name");
               Reslut_Houses_Stars_Flag[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Stars")).getJSONObject(i).getString("Flag");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String final_output =  Reslut_Sex + "\n" + Reslut_Age + "\n" +  Reslut_Birth +"\n" + Reslut_LunarBirth + "\n" + Reslut_FateManner + Reslut_FateSex +"\n"+Reslut_Houses_Name+"\n"+Reslut_Houses_Decade+ Reslut_Houses_GanZhi+"\n";
        for (int i=0;i<count;i++){
           final_output +=   Reslut_Houses_Stars_Name[i];
           final_output += " ";
            final_output +=   Reslut_Houses_Stars_Flag[i];
           final_output += "\n";
         }
        TextView output;
        output = (TextView)findViewById(R.id.textView);  // 面板上的TextView
        output.setText(final_output); //結果結果結果
    }

    public class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) { // 任務輸入的值
            this.publishProgress(null);
            try {
                // 連接httpget輸入
                HttpGet httpget = new HttpGet(params[0]);           // 剛剛輸入的url
                HttpClient client = new DefaultHttpClient();        //建立Httpclinet
                HttpResponse response = client.execute(httpget);    //建立response
                HttpEntity resEntityGet = response.getEntity();     //抓取回傳值
                if (resEntityGet != null) {
                    return EntityUtils.toString(resEntityGet, "utf-8"); // 有抓取到回傳值，並用utf-8編碼回傳
                }

            } catch (Exception e) {
                return e.toString();
            }
            return null;  // 沒抓到任何東西
        }

        @Override
        protected void onPostExecute(String text){  // doInBackground的結果會傳至這個涵式
            JsonTrasnfer_output(text);
        }

        @Override
        protected void onProgressUpdate(String... result){

        }
    }

}

package com.example.administrator.robot.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.robot.R;
import com.example.administrator.robot.adpater.RecyclerViewAdapter;
import com.example.administrator.robot.bean.Ask;
import com.example.administrator.robot.bean.Chat;
import com.example.administrator.robot.bean.Take;
import com.example.administrator.robot.net.Api;
import com.example.administrator.robot.util.L;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //  聊天消息列表
    private RecyclerView recyclerView;

    //  输入框
    private EditText editText;

    //  发送按钮
    private Button mButton;

    //倾听按钮
    private Button mListenButton;

    //    对话信息集合
    private List<Chat> list = new ArrayList<>();

    //    适配器
    private RecyclerViewAdapter recyclerViewAdapter;

    // 我说的话转化为文本
    public static  String mResult;

    public static String text;

    private  SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Integer messageAmount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //1、获取SharedPreferences对象。SharedPreferences是一个接口，程序无法直接创建SharedPreferences对象，只能通过Context提供的getSharedPreferences()方法。
         sharedPreferences = getSharedPreferences("message", MODE_PRIVATE);

        //2、获取SharedPreferences.Editor对象，用于写数据。本步骤只对写数据有必要。
         editor = sharedPreferences.edit();



/**
 *
 *
 * 漏了这一行，直接完蛋
 */
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5d1ab2fa");//用于初始化即创建语音配置对象，只有初始化后才
        // 可以使用MSC的各项服务。建议将初始化放在程序入口处
//      初始化数据
        initView();
//       加载数据
        initData();
//      设置布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(this, list);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    /**
     * 加载列表布局数据
     */
    private void initData() {

        /**
         * 几条静态的数据
         */
        Chat c1 = new Chat("我很想她", Chat.TYPE_SENT);
        list.add(c1);
        Chat c2 = new Chat("加油吧 一定会遇见的", Chat.TYPE_RECEIVED);
        list.add(c2);
        Chat c3 = new Chat("你可以把我想说的话告诉她吗", Chat.TYPE_SENT);
        list.add(c3);
        Chat c4 = new Chat("什么话", Chat.TYPE_RECEIVED);
        list.add(c4);


    }




    private void initView() {


        mListenButton = findViewById(R.id.btn_listen);
        recyclerView = findViewById(R.id.recycler);
        editText = findViewById(R.id.et_text);
        mButton = findViewById(R.id.btn_send);
        mButton.setOnClickListener(this);
        mListenButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:

                /**
                    无论是倾听还是发送，最后把消息发到右边，都要再点一次btn_send

                 */
                /**
                 * 1，获取输入框的内容
                 * 2，判断是否为空
                 * 3，发送后清空当前的输入框
                 */
//              1,获取输入框的内容
                text = editText.getText().toString();

//              2,判断是否为空

//                  把要发送的数据添加到addData方法中，并把数据类型也填入，这里我们的类型是TYPE_SENT，发送数据类型
                    addData(text, Chat.TYPE_SENT);
//                  最终发送的时候才清空输入框
                    editText.setText("");
//                  把发送的文本数据传递到request方法中，请求数据

                            request(text);//     在你倾听完并且点击发送或只是写的文本之后      统一在这里请求对方的数据，并显示在左边



                text="";//将text初始化
                mResult="";//语音的文本初始化
                break;

            case R.id.btn_listen:
                /**
                 * 如果你点击了倾听按钮的话，
                 * 调用initSpeech()方法，
                 * 将mResult文本添加到AddData中
                 * */


/**
 * 当点击了listen按纽，进入initSpeech,表面意思，  初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
 */

                initSpeech(MainActivity.this);
//                   new Thread()
//                   {
//                       @Override
//                       public void run() {
//                           super.run();
//
//
//                           if (!TextUtils.isEmpty(mResult)) {
////                  把要发送的数据添加到addData方法中，并把数据类型也填入，这里我们的类型是TYPE_SENT，发送数据类型
//
//
//                           }
//
//                       }
//                   }.start();



//                    addData(mResult, Chat.TYPE_SENT);
//                  清空输入框

//                  把发送的文本数据传递到request方法中，请求数据,得到对方说的话

                    //request 里面有addData方法，将收到





        break;


        }
    }

    /**
     * 通过传递进来的test和type创建数据实体类，添加到聊天数据集合list中
     *
     */

    /**
     * 在onResponse方法里面调用
     *
     */
    private void addData(String text, int type) {


        Chat c = new Chat(text, type);
        list.add(c);
        //当有新消息时，刷新显示

        messageAmount++;


        recyclerViewAdapter.notifyItemInserted(list.size() - 1);
        //定位的最后一行
        recyclerView.scrollToPosition(list.size() - 1);
        editor.putString(messageAmount.toString(),text);
        editor.commit();

        SharedPreferences preferences= getSharedPreferences("message", Context .MODE_PRIVATE);
        String one=preferences.getString(messageAmount.toString(),"");
        L.d("消息"+one);


    }

    /**
     * 请求数据
     *
     *  text ：输入框的发送数据
     */
    private void request(String text) {
//      把输入的文本数据存储在请求实体类中
        Ask ask = new Ask();
        Ask.UserInfoBean info = new Ask.UserInfoBean();
        info.setApiKey("c00282de107144fb940adab994d9ff98");//将机器人的key值填入
        info.setUserId("225167");//将用户id填入
        ask.setUserInfo(info);
        Ask.PerceptionBean.InputTextBean pre = new Ask.PerceptionBean.InputTextBean(text);//将要发送给机器人书文本天趣
        ask.setPerception(new Ask.PerceptionBean(pre));

//       创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.tuling123.com/")//设置网络请求url，后面一段写在网络请求接口里面
                .addConverterFactory(GsonConverterFactory.create())//Gson解析
                .build();
//       创建网络请求接口的实例
        Api api = retrofit.create(Api.class);
//      Take为响应实体类，用来接受机器人返回的回复数据
        Call<Take> call = api.request(ask);
//
        call.enqueue(new Callback<Take>() {
//          请求成功
            @Override
            public void onResponse(Call<Take> call, Response<Take> response) {
//              接受到的机器人回复的数据
                String mText= response.body().getResults().get(0).getValues().getText();
/**
 *          把接受到的数据传入addData方法中，类型是TYPE_RECEIVED接受数据
 */

                addData(mText, Chat.TYPE_RECEIVED);


                L.d("接受到的机器人回复的数据： "+mText);
            }
//            请求失败
            @Override
            public void onFailure(Call<Take> call, Throwable t) {
                L.d("请求失败： "+t.toString());
            }
        });
    }

    /**
     *
     * 科大讯飞的相关实现：：

     */

    public void initSpeech(final Context context) {

        /**
         * 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
         */

        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, null);
        //2.设置accent、language等参数
        //设置语音输入语言，zh_cn为简体中文
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

        //设置结果返回语言:普通话
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");



        //3.开始识别并设置监听器
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                if (!isLast) {
                    //解析语音
                    //返回的result为识别后的汉字,直接赋值到TextView上即可
                    String result = parseVoice(recognizerResult.getResultString());
                    mResult=result;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            editText.setText(mResult);
                            editText.setSelection(editText.getText().length());
                        }
                    }).start();

                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        });
        //4.显示dialog，接收语音输入
        mDialog.show();
    }



    /**
     * 解析语音json数据
     */
    public String parseVoice(String resultString) {
            Gson gson = new Gson();
            Voice voiceBean = gson.fromJson(resultString, Voice.class);

            StringBuffer sb = new StringBuffer();
            ArrayList<Voice.WSBean> ws = voiceBean.ws;
            for (Voice.WSBean wsBean : ws) {

                //把词给连起来
                String word = wsBean.cw.get(0).w;
                sb.append(word);
            }
            return sb.toString();
    }
    public class Voice {

        //ws：词 类型：array 里面有一个ison对象cw
        public ArrayList<WSBean> ws;

        class WSBean {

            //cw:中文分词 类型:array
            public ArrayList<Voice.CWBean> cw;


        }

        class CWBean {

            //w:单字  类型：String
            public String w;
        }
    }



}

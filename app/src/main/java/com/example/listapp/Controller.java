package com.example.listapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class Controller {
    Activity activity;
    Stack<String> history;
    String url;

    String colorCode;

    WebView myWebview;

    private String homePage ="https://www.google.com";
    private ArrayList<Message> bookmarkList = new ArrayList<Message>();
    private ArrayList<Message> historyList = new ArrayList<Message>();
    private ListViewAdapter listViewAdapterH;
    private ListViewAdapter listViewAdapterB;

    private int themeIndex = 0;
    private void setThemeColor(int themeIndex){
        if (themeIndex == 0){
            this.activity.setTheme(R.style.Theme_ListApp);
        }
        if (themeIndex == 1){
            this.activity.setTheme(R.style.Theme_ListApp2);
        }
        if (themeIndex == 2){
            this.activity.setTheme(R.style.Theme_ListApp3);
        }
    }

    public Controller(Activity activity){
        this.activity = activity;
        this.history = new Stack<>();
        setupHomeScreen();
    }

//    public String getCurrentUrl(){
//        getHistory();
//        int maxIndex = historyList.size()-1;
//        String lastUrl = historyList.get(maxIndex).getSubtitle();
//        return lastUrl;
//    }

    private void setupHomeScreen(){
        setThemeColor(themeIndex);
        activity.setContentView(R.layout.activity_main);
        EditText input = (EditText) activity.findViewById(R.id.urlinput);

        WebViewClient myWebViewClient = new WebViewClient();
        myWebview = (WebView) activity.findViewById(R.id.webview);
        WebSettings webSettings = myWebview.getSettings();

        WebChromeClient webChromeClient = new WebChromeClient() {
            public void onReceivedTitle(WebView view, String title){
                super.onReceivedTitle(view, title);
                input.setText(view.getUrl());
            }
        };

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        myWebview.setWebViewClient(myWebViewClient);
        myWebview.setWebChromeClient(webChromeClient);
        myWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        myWebview.loadUrl(homePage);



        input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d("INPUT","url entered: "+input.getText());
                    // Perform action on key press
                    url = input.getText().toString();
                    history.push(url);
                    myWebview.loadUrl(url);
                    input.setText(url);
                    return true;
                }
                return false;
            }
        });


        input.setText(myWebview.getUrl());



        Button back = (Button) this.activity.findViewById(R.id.backButton);
        back.setOnClickListener((view -> {
            if (myWebview.canGoBack()){
                myWebview.goBack();
            }
        }));

        Button forward = (Button)this.activity.findViewById(R.id.forwardButton);
        forward.setOnClickListener((view -> {
            if(myWebview.canGoForward()){
                myWebview.goForward();
            }
        }));

        Button addBookmark = (Button)this.activity.findViewById(R.id.addBookButton);
        addBookmark.setOnClickListener((view -> {
            addBookmark();
        }));

        ImageView home = (ImageView)this.activity.findViewById(R.id.homeIcon);
        home.setOnClickListener((view -> {
            setupHomeScreen();
            myWebview.loadUrl(homePage);
        }));

        ImageView history = (ImageView)this.activity.findViewById(R.id.historyIcon);
        history.setOnClickListener((view -> {
            setHistoryPage();
        }));

        ImageView printBookmark = (ImageView)this.activity.findViewById(R.id.bookmarkIcon);
        printBookmark.setOnClickListener((view -> {
            setBookmarkPage();
        }));

        ImageView set = (ImageView) this.activity.findViewById(R.id.setIcon);
        set.setOnClickListener((view -> {
            setSettingPage();
        }));

    }

    private void setHistoryPage(){
        activity.setContentView(R.layout.history);
        getHistory();

        homeButton();
        backButton();
        clearHisButton();

    }

    private void setBookmarkPage(){
        activity.setContentView(R.layout.bookmark);
        getHistory();

        homeButton();
        backButton();
        clearBookButton();

        getBookmark();
    }

    private void setSettingPage(){
        activity.setContentView(R.layout.set);

        //change the homepage by input url
        EditText input2 = (EditText) this.activity.findViewById(R.id.urlinput2);
        input2.setText(homePage);
        Button changeHome = (Button)this.activity.findViewById(R.id.updateHome);
        changeHome.setOnClickListener((view -> {
            String newHome = input2.getText().toString();
            if(!newHome.equals("")){
                homePage = newHome;
            }
        }));

        //change theme color
        chooseColor();

        homeButton();

    }

    public void chooseColor(){
        Button choose1 = (Button)this.activity.findViewById(R.id.theme1);
        choose1.setOnClickListener((view -> {
            themeIndex=1;
            setupHomeScreen();
            setSettingPage();
        }));

        Button choose2 = (Button)this.activity.findViewById(R.id.theme2);
        choose2.setOnClickListener((view -> {
            themeIndex=2;
            setupHomeScreen();
            setSettingPage();
        }));

        Button choose3 = (Button)this.activity.findViewById(R.id.theme3);
        choose3.setOnClickListener((view -> {
            themeIndex=0;
            setupHomeScreen();
            setSettingPage();
        }));
    }

    public void homeButton(){
        ImageView home = (ImageView)this.activity.findViewById(R.id.switchToHome);
        home.setOnClickListener((view -> {
            setupHomeScreen();
        }));
    }

    public void backButton(){
        ImageView back = (ImageView) this.activity.findViewById(R.id.back);
        back.setOnClickListener((view -> {
            activity.setContentView(R.layout.activity_main);
            setupHomeScreen();
            if(!historyList.isEmpty()){
                int index = historyList.size()-1;
                myWebview.loadUrl(historyList.get(index).getSubtitle());
            } else {
                myWebview.loadUrl(homePage);
            }
        }));
    }

    public void clearBookButton(){
        ImageView edit = (ImageView) this.activity.findViewById(R.id.clearIcon);
        edit.setOnClickListener((view -> {
            bookmarkList.clear();

            ListView list1 = (ListView)this.activity.findViewById(R.id.listview);
            if(listViewAdapterB != null){
                Message empty = new Message("Empty","Nothing Here");
                ArrayList<Message> messages = new ArrayList<>();
                messages.add(empty);
                ListViewAdapter listViewAdapter1 =new ListViewAdapter(this.activity, messages);
                list1.setAdapter(listViewAdapter1);
            }

        }));
    }

    public void clearHisButton(){
        ImageView clear = (ImageView) this.activity.findViewById(R.id.clearIcon);
        clear.setOnClickListener((view -> {
            historyList.clear();

            ListView list1 = (ListView)this.activity.findViewById(R.id.listview);
            if(listViewAdapterH != null){
                Message empty = new Message("Empty","Nothing Here");
                ArrayList<Message> messages = new ArrayList<>();
                messages.add(empty);

                ListViewAdapter listViewAdapter1 =new ListViewAdapter(this.activity, messages);
                list1.setAdapter(listViewAdapter1);
            }
        }));
    }


    public void getHistory() {
        ListView hList = (ListView)this.activity.findViewById(R.id.listview);

        history.clear();

        WebBackForwardList allHistory = myWebview.copyBackForwardList();
        for(int i=0; i < allHistory.getSize(); i++){
            String title1 = allHistory.getItemAtIndex(i).getTitle();
            String subTitle1 = allHistory.getItemAtIndex(i).getUrl();
            Message newItem = new Message(title1, subTitle1);
            historyList.add(newItem);
        }

        listViewAdapterH = new ListViewAdapter(this.activity, historyList);
        hList.setAdapter(listViewAdapterH);

        hList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setupHomeScreen();
                myWebview.loadUrl(historyList.get(i).getSubtitle());
            }
        });

    }

    public void addBookmark(){
        String title1 = myWebview.getTitle();
        String subtitle1 =myWebview.getUrl();
        Message bookmark1 = new Message(title1,subtitle1);
        bookmarkList.add(bookmark1);
    }
    public void getBookmark(){
        ListView bList = (ListView) this.activity.findViewById(R.id.listview);
        listViewAdapterB = new ListViewAdapter(this.activity, bookmarkList);
        bList.setAdapter(listViewAdapterB);
        bList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setupHomeScreen();
                myWebview.loadUrl(bookmarkList.get(i).getSubtitle());
            }
        });
    }

    public void onBackPress(){
        Log.d("ONBACK","Loading previous site if exists");
        if (myWebview.canGoBack()){
            myWebview.goBack();
        } else {
            Toast.makeText(activity, activity.getString(R.string.shut_down), Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

}

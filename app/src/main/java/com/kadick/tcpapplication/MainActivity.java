package com.kadick.tcpapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private EditText theMessageToBeSent;
    private Button sendMessage, connectServer, disconnectServer;
    private TextView receiveMessage, responseFromServer;
    private TcpClient myClient = new TcpClient();
    private ConnectTask theConnect;
    public TcpClient mTcpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theMessageToBeSent = findViewById(R.id.send_message_view);
        sendMessage = findViewById(R.id.send_message_button);
        receiveMessage = findViewById(R.id.receive_message_view);
        responseFromServer = findViewById(R.id.response_from_server_view);
        connectServer = findViewById(R.id.connect_server);
        disconnectServer = findViewById(R.id.disconnect_server);




        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(theMessageToBeSent.getText().toString());
                    theMessageToBeSent.setText("");
                }
            }
        });

        connectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConnectTask().execute("Create Connection");
            }
        });

        disconnectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTcpClient != null) {
                    mTcpClient.stopClient();
                }
            }
        });
    }

    class ConnectTask extends AsyncTask<String, String, TcpClient> {



        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageAction() {
                @Override
                public void sendTheMessage(String message) {
                    mTcpClient.sendMessage(theMessageToBeSent.getText().toString());
                    theMessageToBeSent.setText("");
                }

                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                    receiveMessage.setText(message);
                }



            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....
            responseFromServer.setText("Server Response: "+values[0]);


        }
    }
}
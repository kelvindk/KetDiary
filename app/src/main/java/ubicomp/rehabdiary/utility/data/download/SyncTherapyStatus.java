package ubicomp.rehabdiary.utility.data.download;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import ubicomp.rehabdiary.fragments.event.EventLogStructure;
import ubicomp.rehabdiary.utility.data.db.DatabaseControl;
import ubicomp.rehabdiary.utility.data.structure.TriggerItem;
import ubicomp.rehabdiary.utility.data.upload.ServerUrl;
import ubicomp.rehabdiary.utility.system.PreferenceControl;

/**
 * Created by yu on 2016/7/27.
 */
public class SyncTherapyStatus {
    private static String SERVER_URL_THERAPY_STATUS = null;
    private static String TAG = "SyncTherapyStatus";

    private ResponseHandler<String> responseHandler;
    private Context context;

    private ArrayList<EventLogStructure> list = new ArrayList<EventLogStructure>();

    public SyncTherapyStatus(Context context) {
        this.context = context;
        SERVER_URL_THERAPY_STATUS = ServerUrl.SERVER_URL_THERAPY_STATUS();
        responseHandler = new BasicResponseHandler();
    }

    public void update()
    {
        getStatusList();
        syncDataBase();
        return;
    }

    public void getStatusList()
    {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            InputStream instream = context.getResources().openRawResource(
                    ServerUrl.SERVER_CERTIFICATE());
            try {
                trustStore.load(instream, null);
            } finally {
                instream.close();
            }
            SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
            Scheme sch = new Scheme("https", socketFactory, 443);

            httpClient.getConnectionManager().getSchemeRegistry().register(sch);

            HttpPost httpPost = new HttpPost(SERVER_URL_THERAPY_STATUS);
            httpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
                    3000);

            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpPost);
            String responseString = responseHandler
                    .handleResponse(httpResponse);
            int httpStatusCode = httpResponse.getStatusLine().getStatusCode();

            //responseString = unicode2String(responseString);


            String userId = "";
            long createTime = 0;
            int reviseOriginalBehavior = 0;
            int reviseOriginalEmotion = 0;
            int reviseOriginalThought = 0;
            int reviseExpectedBehavior = 0;
            int reviseExpectedEmotion = 0;
            int reviseExpectedThought = 0;

            if (responseString != null && httpStatusCode == HttpStatus.SC_OK) {

                StringTokenizer st = new StringTokenizer(responseString,"[],\"");

                int t = 0;
                while(st.hasMoreTokens()) {
                    if(t == 0)
                    {
                        userId = st.nextToken();
                        Log.d(TAG, "userId : "+ userId);
                    }
                    else if(t == 1)
                    {
                        createTime = Long.parseLong(st.nextToken());
                        Log.d(TAG, "createTime : "+ createTime);
                    } else if(t == 2)
                    {
                        reviseOriginalBehavior = Integer.parseInt(st.nextToken());
                        Log.d(TAG, "reviseOriginalBehavior : "+ reviseOriginalBehavior);
                    } else if(t == 3)
                    {
                        reviseOriginalEmotion = Integer.parseInt(st.nextToken());
                        Log.d(TAG, "reviseOriginalEmotion : "+ reviseOriginalEmotion);
                    } else if(t == 4)
                    {
                        reviseOriginalThought = Integer.parseInt(st.nextToken());
                        Log.d(TAG, "reviseOriginalThought : "+ reviseOriginalThought);
                    } else if(t == 5)
                    {
                        reviseExpectedBehavior = Integer.parseInt(st.nextToken());
                        Log.d(TAG, "reviseExpectedBehavior : "+ reviseExpectedBehavior);
                    } else if(t == 6)
                    {
                        reviseExpectedEmotion = Integer.parseInt(st.nextToken());
                        Log.d(TAG, "reviseExpectedEmotion : "+ reviseExpectedEmotion);
                    } else if(t == 7)
                    {
                        reviseExpectedThought = Integer.parseInt(st.nextToken());
                        Log.d(TAG, "reviseExpectedThought : "+ reviseExpectedThought);
                    }
                    t++;

                    if(t == 8)
                    {
                        t = 0;

                        if(PreferenceControl.getUID().equals(userId)) {
                            EventLogStructure data = new EventLogStructure();
                            data.createTime = Calendar.getInstance();
                            data.createTime.setTimeInMillis(createTime);
                            data.reviseOriginalBehavior = (reviseOriginalBehavior > 0);
                            data.reviseOriginalEmotion = (reviseOriginalEmotion > 0);
                            data.reviseOriginalThought = (reviseOriginalThought > 0);
                            data.reviseExpectedBehavior = (reviseExpectedBehavior > 0);
                            data.reviseExpectedEmotion = (reviseExpectedEmotion > 0);
                            data.reviseExpectedThought = (reviseExpectedThought > 0);

                            list.add(data);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    public void syncDataBase(){
        int len = list.size();
        DatabaseControl db = new DatabaseControl();

        for (int i = 0; i < len; i++)
        {
            if(i == 0 || (list.get(i).createTime != list.get(i - 1).createTime ))
            {
                db.updateEventLogTherapyStatus(list.get(i));
            }
        }
    }
}

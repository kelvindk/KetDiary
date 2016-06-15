package ubicomp.ketdiary.utility.data.structure;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ubicomp.ketdiary.utility.data.db.DatabaseControl;

/**
 * Created by yu on 16/5/25.
 */
public class DropEditText {

    private Context context;
    ListView listView;
    EditText editText;
    Resources resource;
    boolean show;
    ImageView button;
    int pageNum, item;
    String[] content = new String[3];
    String[] contentCans = {"吸K", "找K", "想吸K",
                            "好痛苦...吸一點點就好","吸了會比較快樂","明天再開始戒",
                            "避免到有Ｋ的地方", "運動", "睡覺",
                            "再吸我會比現在更痛苦，膀胱爛光", "吸了會被醫師知道", "我的親友是我的力量"};
    DatabaseControl db;
    public DropEditText(ListView listView, ImageView button, EditText editText, int pageNum, int item, Context context)
    {
        this.listView = listView;
        this.button = button;
        this.pageNum = pageNum;
        this.context = context;
        this.editText = editText;
        this.show = false;
        this.item = item;

        resource = context.getResources();
        db = new DatabaseControl();
    }

    public void showHide()
    {
        if(!show)
            listView.setVisibility(View.VISIBLE);
        else
            listView.setVisibility(View.GONE);
        show = !show;
    }

//    public void setting()
//    {
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                showHide();
//            }
//        });
//
//        for (int i = 0; i < 3; i++)
//            content[i] = contentCans[(pageNum - 1) * 3 + i];
//
//        getHistory();
//
//        ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.my_listitem, content);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                ClickLog.Log(ClickLogId.DAYBOOK_ADDNOTE_SELECT_ITEM);
//                TextView c = (TextView) view.findViewById(android.R.id.text1);
//                String playerChanged = c.getText().toString();
//
//                editText.setText(playerChanged);
//                listView.setVisibility(View.GONE);
//                show = false;
//            }
//
//        });
//        setListViewHeightBasedOnItems(listView);
//
//        listView.setVisibility(View.GONE);
//        show = false;
//    }

    public boolean setListViewHeightBasedOnItems(ListView listview) {

        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            int totalItemsHeight = 0;
            Log.d("GG", "here item number : "+ numberOfItems);
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listview);

                item.measure(0, 0);

                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listview.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            //params.height = totalItemsHeight + totalDividersHeight;
            params.height = (int) (convertDpToPixel((float)40)* numberOfItems) + totalDividersHeight;

            listview.setLayoutParams(params);
            listview.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public float convertDpToPixel(float dp){
        float px = dp * getDensity();
        return px;
    }

    public float getDensity(){

        DisplayMetrics metrics = resource.getDisplayMetrics();
        return metrics.density;
    }

    public void getHistory(){
        History[] data = db.getHistory(item, pageNum);
        Log.d("GG", "get History : "+ data);
        if(data == null)
            return;

        content[0] = data[0].getContent();
        int now = 1;
        for (int i = 1; i < data.length; i++)
        {
            if(now == 3)
                break;

            if(data[i].getContent() == data[i-1].getContent())
                continue;

            content[now] = data[i].getContent();
            now++;
        }
    }

    public void setItem(int v){
        this.item = v;
    }



}

package hugues.marchal.cheaty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import hugues.marchal.cheaty.R;

/**
 * Created by makarov on 09/04/16.
 */
public class WifiExpandableListViewAdapter extends BaseExpandableListAdapter{

    private Map<String,String> allItems;
    private ArrayList<String> childItems;
    private ArrayList<String> groupItems;
    private Context context;
    private LayoutInflater inflater;


    public WifiExpandableListViewAdapter(Context ctx, LayoutInflater layoutInflater, Map<String, String> completeMap,ArrayList<String> group){
        this.allItems=completeMap;
        this.groupItems=group;
        //this.childItems=child;
        this.context=ctx;
        this.inflater=layoutInflater;
    }

    @Override
    public int getGroupCount() {
        return this.groupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.allItems.get(groupItems.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.wifi_explist_group_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.wifiGroupTV);
        textView.setText(groupItems.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition, childPosition);
        if(convertView==null){
            convertView = inflater.inflate(R.layout.wifi_explist_child_item, null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.wifiChildTV);
        textView.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

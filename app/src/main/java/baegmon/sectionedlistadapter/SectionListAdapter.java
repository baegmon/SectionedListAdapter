package baegmon.sectionedlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Simon on 06/12/2015.
 */
public class SectionListAdapter extends BaseAdapter implements Filterable{

    private LayoutInflater inflater;
    private Context context;
    private Map<String, List<String>> sections =
            new LinkedHashMap<>();

    private Map<String, List<String>> filteredSections =
            new LinkedHashMap<>();

    private static final int TYPE_LIST_ITEM = 0;
    private static final int TYPE_LIST_HEADER = 1;
    private static final int TYPE_COUNT = TYPE_LIST_HEADER + 1;

    private Filter filter;


    private static class ViewHolder {
        ImageView thumbnail;
        TextView header;
        TextView title;

    }

    public void addSection(String header, List<String> items) {
        this.sections.put(header, items);
        this.filteredSections.put(header, items);

        notifyDataSetChanged();
    }

    public SectionListAdapter(Context context){
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getCount() {
        int totalCount = 0;

        for (List<String> sections : this.filteredSections.values()) {
            totalCount += sections.size() + 1;
        }

        return totalCount;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = -1;

        for (String header : this.filteredSections.keySet()) {

            List<String> items = this.filteredSections.get(header);
            int sectionCount = items.size() + 1; // Number of items + header

            if (position < sectionCount) {
                viewType = position == 0 ? TYPE_LIST_HEADER : TYPE_LIST_ITEM;
                return viewType;
            } else {
                position -= sectionCount;
            }

        }

        return -1;
    }

    @Override
    public Object getItem(int position) {
        Object item;

        for (String header : this.filteredSections.keySet()) {
            List<String> items = this.filteredSections.get(header);
            int sectionCount = items.size() + 1;

            if (position < sectionCount) {
                item = position == 0 ? header : items.get(position - 1);
                return item;
            } else {
                position -= sectionCount;
            }
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isHeader(String item){

        if(item.length() == 1){
            return true;
        } else {
            return false;
        }
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = this.getItem(position).toString();
        //String description = getDescription(item);
        Boolean isHeader = isHeader(item);
        ViewHolder viewHolder;


        if (convertView == null) {
            viewHolder = new ViewHolder();

            int resourceId = isHeader ?
                    R.layout.header_layout :
                    R.layout.item_layout;

            convertView = this.inflater.inflate(resourceId, parent, false);

            if (isHeader) {
                viewHolder.header = (TextView) convertView.findViewById(R.id.TextView_header);

            } else {
                viewHolder.title = (TextView) convertView.findViewById(R.id.TextView_title);
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.image_thumbnail);

                viewHolder.thumbnail.setId(position);
            }

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (isHeader) {
            viewHolder.header.setText(item);
        } else {
            viewHolder.title.setText(item);
            viewHolder.thumbnail.setImageResource(R.drawable.nophoto);

        }


        return convertView;
    }

    @Override
    public Filter getFilter() {

        if (this.filter == null) {
            this.filter = new dataFilter();
        }

        return this.filter;
    }

    private class dataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                Map<String, List<String>> newSections =
                        new LinkedHashMap<>();

                for (String header : sections.keySet()) {
                    List<String> data = sections.get(header);
                    List<String> filteredData = new ArrayList<>();

                    for (String name : data) {
                        if (name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredData.add(name);
                        }
                    }

                    if (filteredData.size() > 0) {
                        newSections.put(header, filteredData);
                    }
                }
                filterResults.values = newSections;
                filterResults.count = newSections.size();
            } else {
                filterResults.values = sections;
                filterResults.count = sections.size();
            }

            return filterResults;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            Map<String, List<String>> filteredValues =
                    (Map<String, List<String>>) filterResults.values;
            Map<String, List<String>> tempSections = new HashMap<>();
            tempSections.clear();
            filteredSections.clear();
            tempSections.putAll(filteredValues);
            filteredSections = new TreeMap<>(tempSections);

            notifyDataSetChanged();
        }
    }
}

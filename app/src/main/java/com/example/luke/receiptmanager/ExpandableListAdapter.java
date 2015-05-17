package com.example.luke.receiptmanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ldt on 17/05/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<String> listCategories;
    private Map<String, ArrayList<Receipt>> listItems;

    public ExpandableListAdapter(Activity context, List<String> listCategories, Map<String, ArrayList<Receipt>> listItems) {
        this.context = context;
        this.listCategories = listCategories;
        this.listItems = listItems;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //get the current category
        String category = (String) getGroup(groupPosition);
        //create a layoutinflater
        LayoutInflater layoutInflater = context.getLayoutInflater();

        //check if the pass in view is null
        if(convertView == null)
        {
            //inflate the view
            convertView = layoutInflater.inflate(R.layout.list_category, null);
        }

        //get the textview for the category title
        TextView categoryTitle = (TextView)convertView.findViewById(R.id.txtCategoryTitle);

        //set the textview to the title of the category
        if(categoryTitle != null) {
            categoryTitle.setText(category);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //get the current receipt
        Receipt receipt = (Receipt)getChild(groupPosition, childPosition);
        //create a layoutinflater
        LayoutInflater layoutInflater = context.getLayoutInflater();

        //check if the pass in view is null
        if(convertView == null)
        {
            //inflate the view
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        //get the textview for the receipts title
        TextView receiptTitle = (TextView)convertView.findViewById(R.id.txtReceiptTitle);
        //get the imageview for the receipt
        ImageView receiptImage = (ImageView)convertView.findViewById(R.id.imgReceiptImage);

        //set the textview to the title of the receipt
        if(receiptTitle != null) {
            receiptTitle.setText(receipt.Title);
        }
        //set the imageView for the receipt
        //receiptImage.setImageBitmap();

        return convertView;
    }

    /*    The rest of these methods are just defaults (we might need to modify them later    */

    @Override
    public int getGroupCount() {
        return listCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String category = listCategories.get(groupPosition);
        return listItems.get(category).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listCategories.get(groupPosition);
    }

    @Override
    public Receipt getChild(int groupPosition, int childPosition) {
        String category = listCategories.get(groupPosition);
        return (Receipt)listItems.get(category).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

package com.example.appwidgetcollectiondemo;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class ListWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsFactory {

        private static final int mCount = 10;
        private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
        private Context mContext;
        private int mAppWidgetId;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Initialize the data set.
        @Override
        public void onCreate() {
            System.out.println("onCreate() is called for " + mAppWidgetId);

            // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
            // for example downloading or creating content etc, should be deferred to onDataSetChanged()
            // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
            for (int i = 1; i <= mCount; i++) {
                mWidgetItems.add(new WidgetItem("Item" + i));
            }
            // We sleep for 3 seconds here to show how the empty view appears in the interim.
            // The empty view is set in the ListWidgetProvider and should be a sibling of the
            // collection view.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onDestroy() {
            System.out.println("onDestroy() is called for " + mAppWidgetId);
            // In onDestroy() you should tear down anything that was setup for your data source,
            // eg. cursors, connections, etc.
            mWidgetItems.clear();
        }

        @Override
        public int getCount() {
            System.out.println("WidgetId " + mAppWidgetId);
            return mCount;
        }

        // Given the position (index) of a WidgetItem in the array, use the item's text value in
        // combination with the app widget item XML file to construct a RemoteViews object.
        @Override
        public RemoteViews getViewAt(int position) {
            System.out.println("getViewAt() is called for " + mAppWidgetId);
            // position will always range from 0 to getCount() - 1.
            // construct a remote views item based on our widget item xml file, and set the
            // text based on the position.
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            rv.setTextViewText(R.id.widget_item, mWidgetItems.get(position).text);

            // Next, we set a fill-intent which will be used to fill-in the pending intent template
            // which is set on the collection view in ListWidgetProvider.
            Bundle extras = new Bundle();
            extras.putInt(ListWidgetProvider.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            // Make it possible to distinguish the individual on-click
            // action of a given item
            rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

            // You can do heaving lifting in here, synchronously. For example, if you need to
            // process an image, fetch something from the network, etc., it is ok to do it here,
            // synchronously. A loading view will show up in lieu of the actual contents in the
            // interim.
            try {
                System.out.println("Loading view " + position);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return the remote views object.
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            // You can create a custom loading view (for instance when getViewAt() is slow.) If you
            // return null here, you will get the default loading view.
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
            // on the collection view corresponding to this factory. You can do heaving lifting in
            // here, synchronously. For example, if you need to process an image, fetch something
            // from the network, etc., it is ok to do it here, synchronously. The widget will remain
            // in its current state while work is being done here, so you don't need to worry about
            // locking up the widget.
        }

    }

}

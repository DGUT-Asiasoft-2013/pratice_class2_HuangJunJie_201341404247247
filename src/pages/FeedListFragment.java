package pages;

import java.text.ParsePosition;
import java.util.Random;

import com.example.hello.FeedContentActivity;
import com.example.hello.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FeedListFragment extends Fragment {

	 View view;
	 ListView listView;
	 String data[];
	 
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			if(view==null){
				view=inflater.inflate(R.layout.fragment_page_feeds, null);
			
				listView = (ListView) view.findViewById(R.id.list);
				listView.setAdapter(listAdapter);
				
				Random random=new Random();
				data=new String[10+(random.nextInt()%20)];
				
				for(int i=0;i<data.length;i++){
					data[i]="THIS IS ROW "+random.nextInt();
				}
				
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						onclick(position);
						
					}
				});
			}
			return view;
		}
		BaseAdapter listAdapter = new BaseAdapter() {
			
			@SuppressLint("InflateParams")//标注忽略指定的警告
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = null;
				
				if(convertView==null){
					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					view = inflater.inflate(R.layout.feed, null);	
				}else{
					view = convertView;
				}
				
				TextView text1 = (TextView) view.findViewById(R.id.textView1);
				ImageView img=(ImageView) view.findViewById(R.id.imageView1);
				text1.setText(data[position]);
				
				return view;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return data[position];
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return data==null? 0:data.length;
			}
		};
		void onclick(int position){
			Intent itnt=new Intent(this.getActivity(),FeedContentActivity.class);
			itnt.putExtra("data",data[position]);
			startActivity(itnt);
		}
}

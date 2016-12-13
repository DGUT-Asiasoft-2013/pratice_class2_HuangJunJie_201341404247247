package com.example.hello;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import fragments.MainTabbarFragment;
import fragments.MainTabbarFragment.OnNewClickedListener;
import fragments.MainTabbarFragment.OnTabSelectedListener;
import pages.FeedListFragment;
import pages.MeListFragment;
import pages.NewListActivity;
import pages.NotesListFragment;
import pages.SearchListFragment;

public class HelloWorldActivity extends Activity {
	
	FeedListFragment contentFeedList=new FeedListFragment();
	NotesListFragment contentNoteList=new NotesListFragment();
	SearchListFragment contentSearchPage=new SearchListFragment();
	MeListFragment contentMyProfile=new MeListFragment();
	
	MainTabbarFragment tabbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helloworld);
		
		tabbar=(MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener(){
			@Override
			public void onTabSelected(int index) {
				changeContentFragment(index);
			}
		});
		
		tabbar.setOnNewClickedListener(new OnNewClickedListener() {
			
			@Override
			public void onNewClicked() {
				// TODO Auto-generated method stub
				bringUpEditor();
				
			}
		});
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(tabbar.getSelectedIndex()<0){
		tabbar.setSelectItem(0);
		}
	}

	void changeContentFragment(int index){
		Fragment newFrag=null;
		
		switch (index) {
		case 0:newFrag=contentFeedList;break;
		case 1:newFrag=contentNoteList;break;
		case 2:newFrag=contentSearchPage;break;
		case 3:newFrag=contentMyProfile;break;
			
		default:break;
		}
		if(newFrag==null)return;
		
		getFragmentManager().beginTransaction().replace(R.id.contnet, newFrag).commit();
	}

	void bringUpEditor(){
		Intent itnt=new Intent(this,NewListActivity.class);
		startActivity(itnt);
		overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
	}

    
}

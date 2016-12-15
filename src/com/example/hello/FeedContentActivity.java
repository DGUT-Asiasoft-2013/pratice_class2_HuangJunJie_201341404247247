package com.example.hello;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import api.Server;
import entity.Article;
import entity.Comment;
import entity.Page;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import widgets.AvatarView;

public class FeedContentActivity extends Activity {

	private Article article;
	private Button btnLikes;

	List<Comment> comments;
	int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.feed__article_item);

		TextView username = (TextView) findViewById(R.id.username);
		TextView title = (TextView) findViewById(R.id.title);
		TextView date = (TextView) findViewById(R.id.date);
		TextView text = (TextView) findViewById(R.id.text);
		AvatarView avatar = (AvatarView) findViewById(R.id.avatar);
		ListView listview = (ListView) findViewById(R.id.list);

		article = (Article) getIntent().getSerializableExtra("pos");

		avatar.load(article.getAuthor());
		username.setText(article.getAuthor().getName());
		title.setText(article.getTitle());
		text.setText(article.getText());
		String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
		date.setText(dateStr);

		listview.setAdapter(listAdapter);

		findViewById(R.id.btn_comment).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goComment(article);
			}
		});

		btnLikes = (Button) findViewById(R.id.like);
		btnLikes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Likes();

			}
		});
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams") // 标注忽略指定的警告
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.widget_comment, null);
			} else {
				view = convertView;
			}

			TextView textComment = (TextView) view.findViewById(R.id.list_comment_text);
			TextView textAuthorName = (TextView) view.findViewById(R.id.username);
			TextView textDate = (TextView) view.findViewById(R.id.date);
			AvatarView avatar = (AvatarView) view.findViewById(R.id.avatar);

			Comment comment = comments.get(position);

			textComment.setText(comment.getText());
			textAuthorName.setText(comment.getAuthor().getName());
			avatar.load(comment.getAuthor());

			String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", comment.getCreateDate()).toString();
			textDate.setText(dateStr);

			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return comments.get(position).getId();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return comments.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return comments == null ? 0 : comments.size();
		}
	};

	private boolean isLiked;

	@Override
	protected void onResume() {

		super.onResume();
		reload();
	}

	void reload() {
		checkLiked();
		reloadLikes();
		Request request = Server.requestBuilderWithApi("/article/" + article.getId() + "/comments").get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Comment> data = new ObjectMapper().readValue(arg1.body().string(),
							new TypeReference<Page<Comment>>() {

							});
					runOnUiThread(new Runnable() {
						public void run() {
							FeedContentActivity.this.reloadData(data);
						}
					});

				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							FeedContentActivity.this.onFailure(e);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						FeedContentActivity.this.onFailure(arg1);
					}
				});

			}
		});
	}

	void goComment(Article article) {
		Intent itnt = new Intent(FeedContentActivity.this, CommentActivity.class);
		Article pos = article;
		itnt.putExtra("pos", (Serializable) pos);
		startActivity(itnt);
		overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
	}

	void checkLiked() {
		Request request = Server.requestBuilderWithApi("article/" + article.getId() + "/isliked").get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final String responseString = arg1.body().string();
					Log.d("check liked", responseString);

					final Boolean result = new ObjectMapper().readValue(responseString, Boolean.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(result);
						}
					});
				} catch (final Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onCheckLikedResult(false);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCheckLikedResult(false);
					}
				});
			}
		});
	}

	void onCheckLikedResult(boolean result) {
		isLiked = result;
		btnLikes.setTextColor(result ? Color.BLUE : Color.BLACK);
	}

	void reloadLikes() {
		Request request = Server.requestBuilderWithApi("/article/" + article.getId() + "/likes").get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					String responseString = arg1.body().string();
					final Integer count = new ObjectMapper().readValue(responseString, Integer.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(count);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onReloadLikesResult(0);
						}
					});
				}
			}

			@Override
			public void onFailure(Call arg0, IOException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onReloadLikesResult(0);
					}
				});
			}
		});
	}

	void onReloadLikesResult(int count) {
		if (count > 0) {
			btnLikes.setText("❤(" + count + ")");
		} else {
			btnLikes.setText("❤");
		}
	}

	void Likes() {
		MultipartBody body = new MultipartBody.Builder().addFormDataPart("likes", String.valueOf(!isLiked)).build();

		Request request = Server.requestBuilderWithApi("article/" + article.getId() + "/likes").post(body).build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {

				try {
					Log.d("click favorite", arg1.body().string());
				} catch (Exception e) {
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						reload();
					}
				});
			}
		});
	}

	protected void reloadData(Page<Comment> data) {
		page = data.getNumber();
		comments = data.getContent();
		listAdapter.notifyDataSetInvalidated();
	}

	void onFailure(Exception e) {
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
	}
}
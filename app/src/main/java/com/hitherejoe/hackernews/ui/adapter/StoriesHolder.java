package com.hitherejoe.hackernews.ui.adapter;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.hitherejoe.hackernews.BuildConfig;
import com.hitherejoe.hackernews.R;
import com.hitherejoe.hackernews.data.model.Post;
import com.hitherejoe.hackernews.data.remote.AnalyticsHelper;
import com.hitherejoe.hackernews.ui.activity.CommentsActivity;
import com.hitherejoe.hackernews.ui.activity.UserActivity;
import com.hitherejoe.hackernews.ui.activity.ViewStoryActivity;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

@LayoutId(R.layout.item_story)
public class StoriesHolder extends ItemViewHolder<Post> {

    @ViewId(R.id.container_post)
    View mPostContainer;

    @ViewId(R.id.text_post_title)
    TextView mPostTitle;

    @ViewId(R.id.text_post_author)
    TextView mPostAuthor;

    @ViewId(R.id.text_post_points)
    TextView mPostPoints;

    @ViewId(R.id.text_view_comments)
    TextView mPostComments;

    @ViewId(R.id.text_view_post)
    TextView mViewPost;

    public StoriesHolder(View view) {
        super(view);
    }

    @Override
    public void onSetValues(Post story, PositionInfo positionInfo) {
        mPostTitle.setText(story.title);
        mPostAuthor.setText(Html.fromHtml(getContext().getString(R.string.story_by) + " " + "<u>" + story.by + "</u>"));
        mPostPoints.setText(story.score + " " + getContext().getString(R.string.story_points));
        if (getItem().postType == Post.PostType.STORY && story.kids == null) {
            mPostComments.setVisibility(View.GONE);
        } else {
            mPostComments.setVisibility(View.VISIBLE);
        }
        mViewPost.setVisibility(story.url != null && story.url.length() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSetListeners() {
        mPostAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BuildConfig.DEBUG) AnalyticsHelper.trackUserNameClicked(getContext());
                getContext().startActivity(UserActivity.getStartIntent(getContext(), getItem().by));
            }
        });
        mPostComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BuildConfig.DEBUG) AnalyticsHelper.trackViewCommentsClicked(getContext());
                launchCommentsActivity();
            }
        });
        mViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BuildConfig.DEBUG) AnalyticsHelper.trackViewStoryClicked(getContext());
                launchStoryActivity();
            }
        });
        mPostContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BuildConfig.DEBUG) AnalyticsHelper.trackStoryCardClicked(getContext());
                Post.PostType postType = getItem().postType;
                if (postType == Post.PostType.JOB || postType == Post.PostType.STORY) {
                    launchStoryActivity();
                } else if (postType == Post.PostType.ASK) {
                    launchCommentsActivity();
                }
            }
        });
    }

    private void launchStoryActivity() {
        getContext().startActivity(ViewStoryActivity.getStartIntent(getContext(), getItem()));
    }

    private void launchCommentsActivity() {
        getContext().startActivity(CommentsActivity.getStartIntent(getContext(), getItem()));
    }
}
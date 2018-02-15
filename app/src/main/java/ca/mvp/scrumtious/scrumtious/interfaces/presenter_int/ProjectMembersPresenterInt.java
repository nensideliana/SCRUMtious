package ca.mvp.scrumtious.scrumtious.interfaces.presenter_int;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import ca.mvp.scrumtious.scrumtious.model.Project;
import ca.mvp.scrumtious.scrumtious.view_impl.ProjectListScreenFragment;
import ca.mvp.scrumtious.scrumtious.view_impl.ProjectMembersFragment;

public interface ProjectMembersPresenterInt {
    FirebaseRecyclerAdapter<Project, ProjectMembersFragment.MembersViewHolder> setupMembersAdapter
            (RecyclerView memberList);
}

package com.example.im2.frags.search;




import com.example.common.APP.Fragment;
import com.example.im2.R;
import com.example.im2.activites.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends Fragment implements SearchActivity.SearchFragment{


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}

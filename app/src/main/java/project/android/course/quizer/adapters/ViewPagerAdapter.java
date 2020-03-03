package project.android.course.quizer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentsTitles = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int i)
    {
        return fragments.get(i);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title)
    {
        fragments.add(fragment);
        fragmentsTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return fragmentsTitles.get(position);
    }
}

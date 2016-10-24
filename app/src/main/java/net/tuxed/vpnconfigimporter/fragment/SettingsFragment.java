package net.tuxed.vpnconfigimporter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import net.tuxed.vpnconfigimporter.EduVPNApplication;
import net.tuxed.vpnconfigimporter.R;
import net.tuxed.vpnconfigimporter.entity.Settings;
import net.tuxed.vpnconfigimporter.service.PreferencesService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment which displays the available settings to the user.
 * Created by Daniel Zolnai on 2016-10-22.
 */
public class SettingsFragment extends Fragment {

    @Inject
    protected PreferencesService _preferencesService;

    @BindView(R.id.forceTcpCheckbox)
    protected SwitchCompat _forceTcpSwitch;

    @BindView(R.id.useCustomTabsCheckbox)
    protected SwitchCompat _customTabsSwitch;

    @BindView(R.id.saveButton)
    protected Button _saveButton;

    private Unbinder _unbinder;

    private Settings _originalSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        EduVPNApplication.get(view.getContext()).component().inject(this);
        _unbinder = ButterKnife.bind(this, view);
        _originalSettings = _preferencesService.getAppSettings();
        _customTabsSwitch.setChecked(_originalSettings.useCustomTabs());
        _forceTcpSwitch.setChecked(_originalSettings.forceTcp());
        return view;
    }

    @OnClick({R.id.forceTcpCheckbox, R.id.useCustomTabsCheckbox })
    public void onSettingChanged() {
        boolean useCustomTabs = _customTabsSwitch.isChecked();
        boolean forceTcp = _forceTcpSwitch.isChecked();
        boolean settingsChanged = useCustomTabs != _originalSettings.useCustomTabs() || forceTcp != _originalSettings.forceTcp();
        _saveButton.setEnabled(settingsChanged);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _unbinder.unbind();
    }

    @OnClick(R.id.saveButton)
    protected void onSaveButtonClicked() {
        boolean useCustomTabs = _customTabsSwitch.isChecked();
        boolean forceTcp = _forceTcpSwitch.isChecked();
        _preferencesService.saveAppSettings(new Settings(useCustomTabs, forceTcp));
        Toast.makeText(getContext(), R.string.settings_saved, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }
}
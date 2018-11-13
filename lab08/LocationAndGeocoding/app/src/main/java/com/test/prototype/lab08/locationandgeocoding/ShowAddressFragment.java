package com.test.prototype.lab08.locationandgeocoding;

import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowAddressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_address_fragment, container, false);
    }

    public void setAddress(Address address) {
        ((TextView)getActivity().findViewById(R.id.textAddrName)).setText(address.getFeatureName());
        ((TextView)getActivity().findViewById(R.id.textAddressLine1)).setText(address.getAddressLine(0));
        ((TextView)getActivity().findViewById(R.id.textAddressLine2)).setText(address.getAddressLine(1));
        ((TextView)getActivity().findViewById(R.id.textLocality)).setText(address.getLocality());
        ((TextView)getActivity().findViewById(R.id.textSubAdminArea)).setText(address.getSubAdminArea());
        ((TextView)getActivity().findViewById(R.id.textAdminArea)).setText(address.getAdminArea());
        ((TextView)getActivity().findViewById(R.id.textCountry)).setText(address.getCountryName());
        ((TextView)getActivity().findViewById(R.id.textPostalCode)).setText(address.getPostalCode());
        ((TextView)getActivity().findViewById(R.id.textPhoneNumber)).setText(address.getPhone());
        ((TextView)getActivity().findViewById(R.id.textUrl)).setText(address.getUrl());
    }
}

package com.mobile.messageclone.Ulti;

public interface ActivityUlti {
    public void CloseDrawer(boolean IsClose);
    public void UpdateStatus(String status);
    public void CloseKeyBoard();
    public void ReattachToolbar();

    public void ReattachToolbar(String titleName);
    public void HideAppBar();
}

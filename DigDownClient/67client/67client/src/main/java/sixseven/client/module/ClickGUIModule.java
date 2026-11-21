package sixseven.client.module;

public class ClickGUIModule extends Module {
  public ClickGUIModule() {
    super(
        "ClickGUI",
        "The 67Client menu. In menus, Shift opens it too.",
        ModuleCategory.getInstance());
    this.getKeybindSetting().setValue(344);
    this.setEnabled(true);
  }

  @Override
  public void setEnabled(boolean flag) {
    super.setEnabled(true);
  }
}

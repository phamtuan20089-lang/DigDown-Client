package sixseven.client;

import java.util.List;
import sixseven.client.setting.SettingEntry;

public record CollectionData(String string, List<SettingEntry> list) {

  public CollectionData(String string2, SettingEntry[] settingEntryArray) {
    this(string2, List.of(settingEntryArray));
  }

  public boolean test() {
    return this.list.isEmpty();
  }
}

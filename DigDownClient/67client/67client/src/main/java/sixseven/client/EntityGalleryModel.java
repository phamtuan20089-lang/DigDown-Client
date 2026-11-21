package sixseven.client;

import java.util.ArrayList;
import java.util.List;
import sixseven.client.setting.EntityFilterEntry;
import sixseven.client.setting.EntityTypeSetting;
import sixseven.client.ui.GalleryModel;

public final class EntityGalleryModel implements GalleryModel {
  public final EntityTypeSetting entityTypeSetting;
  public List list;

  public EntityGalleryModel(EntityTypeSetting entityTypeSetting2) {
    this.entityTypeSetting = entityTypeSetting2;
  }

  @Override
  public String getString() {
    return this.entityTypeSetting.getName();
  }

  @Override
  public long computeLong() {
    return this.entityTypeSetting.enabledCount();
  }

  @Override
  public List getList2() {
    if (this.list == null) {
      ArrayList list2 = new ArrayList(this.entityTypeSetting.getList().size());

      for (EntityFilterEntry choiceSettingSupport2 :
          (java.lang.Iterable<EntityFilterEntry>)
              (java.lang.Iterable<?>) (this.entityTypeSetting.getList())) {
        list2.add(new EntityGalleryItem(choiceSettingSupport2));
      }

      this.list = list2;
    }

    return this.list;
  }
}

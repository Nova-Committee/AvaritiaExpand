package committee.nova.avaritia_expand.client.render;

import committee.nova.avaritia_expand.client.model.geo.InfinityBookModel;
import committee.nova.avaritia_expand.common.item.tool.infinity.InfinityBookItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class InfinityBookRender extends GeoItemRenderer<InfinityBookItem> {


    public InfinityBookRender() {
        super(new InfinityBookModel());
    }
}

package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.cmsiw.common.utils.page.PageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;
/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

      IPage<${entity}> pages(PageQuery<${entity}> query);

      boolean save(${entity} entity);

      boolean updateById(${entity} entity);

      @Override
      boolean removeById(Serializable id);

}
</#if>

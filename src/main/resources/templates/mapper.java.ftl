package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import com.clzy.geo.core.common.persistence.CrudDao;
import com.clzy.geo.core.common.persistence.annotation.MyBatisDao;
/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
 @MyBatisDao
public interface ${table.mapperName} extends CrudDao<${entity}> {

}
</#if>

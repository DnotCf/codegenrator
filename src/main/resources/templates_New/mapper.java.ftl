package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import com.clzy.web.crud.persistence.CrudDao;
import com.clzy.web.crud.persistence.annotation.MyBatisDao;
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

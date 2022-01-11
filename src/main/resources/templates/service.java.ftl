package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import ${package.Mapper}.${table.mapperName};
import com.clzy.geo.core.common.service.CrudService;
import org.springframework.stereotype.Service;
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
@Service
public class ${table.serviceName} extends CrudService<${table.mapperName},${entity}> {


}
</#if>

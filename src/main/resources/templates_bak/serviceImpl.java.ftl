package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import com.cmsiw.common.utils.validator.BeanValidator;
import org.springframework.stereotype.Service;
import com.cmsiw.common.utils.page.PageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cmsiw.common.utils.exception.param.ParamException;
/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    @Override
    public IPage<${entity}> pages(PageQuery<${entity}> query) {
        IPage<${entity}> page = new Page<>(query.getPage(), query.getPageSize());
        QueryWrapper<${entity}> wrapper = new QueryWrapper<>(query.getQuery());
        if (query.getOrders() != null) {
            wrapper.orderBy(query.getOrders() !=null, query.getOrders().isAsc(), query.getOrders().getColumns());
        }else {
            wrapper.orderByDesc("operate_time");
        }
        IPage<${entity}> page1 = this.page(page, wrapper);
        return page1;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = ParamException.class)
    @Override
    public boolean save(${entity} entity) {
        BeanValidator.check(entity);
        return super.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = ParamException.class)
    @Override
    public boolean updateById(${entity} entity) {
        BeanValidator.check(entity);
        return super.updateById(entity);
    }
}
</#if>

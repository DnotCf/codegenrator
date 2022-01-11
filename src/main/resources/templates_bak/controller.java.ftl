package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
import com.cmsiw.common.utils.arguments.JsonParam;
import com.cmsiw.common.utils.page.PageQuery;
import com.cmsiw.common.utils.response.ResponseResult;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import java.util.List;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
@Api(tags = "${table.comment!}相关的接口")
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${cfg.service};

    /**
    * showdoc
    *
    * @catalog ${table.comment!}/分页条件排序获取${table.comment!}信息
    * @title 分页条件排序获取${table.comment!}信息
    * @url <#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>
    * @method get
    * @param query 必选 object 多条件分页请求上传参数说明
    * @return
    */
    @ApiOperation(value = "自定义分页条件排序获取${table.comment!}信息",notes = "自定义分页条件排序获取${table.comment!}信息")
    @GetMapping
    public ResponseResult list(@JsonParam("query") PageQuery<${entity}> query) {
        return ResponseResult.success(${cfg.service}.pages(query));
    }

    /**
    * showdoc
    *
    * @catalog ${table.comment!}/修改${table.comment!}信息
    * @title 修改${table.comment!}信息
    * @method put
    * @url <#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/{id}
    * @param id 必选 string 主键
    * @return
    */
    @ApiOperation(value = "修改${table.comment!}信息",notes = "修改${table.comment!}信息")
    @PutMapping
    public ResponseResult update(@RequestBody ${entity} ${cfg.param}) {
        return ResponseResult.success(${cfg.service}.updateById(${cfg.param}));
    }

    /**
    * showdoc
    *
    * @catalog ${table.comment!}/上传${table.comment!}信息
    * @title 上传${table.comment!}信息
    * @method post
    * @url <#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/{id}
    * @param
    * @return
    */
    @ApiOperation(value = "上传${table.comment!}信息",notes = "上传${table.comment!}信息")
    @PostMapping
    public ResponseResult save(@RequestBody ${entity} ${cfg.param}) {
        return ResponseResult.success(${cfg.service}.save(${cfg.param}));
    }

    /**
    * showdoc
    *
    * @catalog ${table.comment!}/根据主键获取${table.comment!}
    * @title 根据主键获取${table.comment!}
    * @method get
    * @url <#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/{id}
    * @param id
    * @return
    */
    @ApiOperation(value = "根据主键获取${table.comment!}信息",notes = "根据主键获取${table.comment!}信息")
    @GetMapping("/{id}")
    public ResponseResult get(@PathVariable("id") String id)
    {
    return ResponseResult.success(${cfg.service}.getById(id));
    }

    /**
    * showdoc
    *
    * @catalog ${table.comment!}/根据主键删除${table.comment!}信息
    * @title 根据主键删除${table.comment!}信息
    * @method delete
    * @url <#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/{id}
    * @param id
    * @return
    */
    @ApiOperation(value = "根据主键删除${table.comment!}信息",notes = "根据主键删除${table.comment!}信息")
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable("id") String id)
    {
        return ResponseResult.success(${cfg.service}.removeById(id));
    }

    /**
    * showdoc
    *
    * @catalog ${table.comment!}/批量插入${table.comment!}
    * @title 批量插入${table.comment!}
    * @method post
    * @url <#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/batch
    * @param id
    * @return
    */
    @ApiOperation(value = "批量插入${table.comment!}信息",notes = "批量插入${table.comment!}信息")
    @PostMapping("/batch")
    public ResponseResult saveBatch(@RequestBody List<${entity}> list) {
        return ResponseResult.success(${cfg.service}.saveBatch(list));
    }

    @ApiOperation(value = "批量更新${table.comment!}信息",notes = "批量更新${table.comment!}信息")
    @PutMapping("/batch")
    public ResponseResult updateBatch(@RequestBody List<${entity}> list) {
        return ResponseResult.success(${cfg.service}.updateBatchById(list));
    }
}
</#if>

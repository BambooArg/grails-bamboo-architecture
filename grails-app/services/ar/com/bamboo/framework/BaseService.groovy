package ar.com.bamboo.framework

import grails.transaction.Transactional

class BaseService {

    private static final Integer MAX_BUNK = 500;

    @Transactional
    boolean save(domainToSave) {
        if (domainToSave.validate()){
            domainToSave.save()
            return true
        }else{
            BaseService.log.debug("""
       El dominio de la clase ${domainToSave.getClass()}  no pasó la validación.
       Se encontraron los siguientes errores: ${domainToSave.errors}
                    """)
            return false
        }
    }

    @Transactional
    public void disabled(domainToDisable){
        domainToDisable.enabled = false
        domainToDisable.save()
    }

     public <T> T getById(Class clazz, Number id){
        return clazz.get(id)
    }

    protected List<Object> listWithLimit(Class clazz,  where,  Map paramsQuery){
        def query = clazz.where(where)
        Integer count = query.count()
        List<Object> objects = query.list(paramsQuery ?: [:])
        return [objects, count]
    }

    protected List<Object> listAll(Class clazz, where){
        def query = clazz.where(where)

        Integer count = query.count()
        if (count != 0){
            List<Object> objects = new ArrayList<Objects>(count)
            boolean continueSearch = true
            Map paramsQuery = [:]
            paramsQuery.max = MAX_BUNK
            paramsQuery.offset = 0
            while(continueSearch){
                objects.addAll(query.list(paramsQuery))
                continueSearch = objects.size() < count
                if (continueSearch){
                    paramsQuery.offset += MAX_BUNK
                }
            }
            return objects
        }else{
            return []
        }
    }

    protected List<Object> listAll(Class clazz, where, Map options){
        def query = clazz.where(where)
        if (options){
            if (options.projections){
                query = query.property(options.projections)
            }
        }
        List<Object> objects = new ArrayList<Objects>()
        boolean continueSearch = true
        Map paramsQuery = [:]
        paramsQuery.max = MAX_BUNK
        paramsQuery.offset = 0
        while(continueSearch){
            List<Object> queryResult = query.list(paramsQuery)
            objects.addAll(queryResult)
            continueSearch = queryResult.size() == MAX_BUNK
            if (continueSearch){
                paramsQuery.offset += MAX_BUNK
            }
        }
        return objects

    }
}

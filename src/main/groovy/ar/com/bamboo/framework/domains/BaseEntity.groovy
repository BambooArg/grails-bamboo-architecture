package ar.com.bamboo.framework.domains

import groovy.transform.CompileStatic

/**
 * Created by orko on 05/08/14.
 */
@CompileStatic
trait BaseEntity implements Serializable{

    boolean enabled
    Date dateCreated
    Date lastUpdated

    void beforeInsert() {
        this.enabled = true
        this.executeMoreBeforeInsert()
    }

    void  executeMoreBeforeInsert(){

    }
}

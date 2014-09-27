package basic2_4_3

import grails.transaction.Transactional
import org.springframework.beans.factory.InitializingBean

@Transactional
class SomeClassService implements InitializingBean {

    static scope = "prototype"

    def x = 0

    void afterPropertiesSet() {
      println "new one?"
    }

    def serviceMethod(params, max) {
      println "${++x > 1 ? 'not':'is'} ok"
      params.max = Math.min(max ?: 10, 100)
      [
        SomeClass.list(params), 
        [
          model: [
              someClassInstanceCount: SomeClass.count()
          ]
        ]
      ]
    }

}

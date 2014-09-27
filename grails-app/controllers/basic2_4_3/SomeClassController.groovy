package basic2_4_3



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SomeClassController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def someClassService

    def index(Integer max) {
        def list, model
        (list, model) = someClassService.serviceMethod(params, max)
        respond list, model
    }

    def show(SomeClass someClassInstance) {
        respond someClassInstance
    }

    def create() {
        respond new SomeClass(params)
    }

    @Transactional
    def save(SomeClass someClassInstance) {
        if (someClassInstance == null) {
            notFound()
            return
        }

        if (someClassInstance.hasErrors()) {
            respond someClassInstance.errors, view:'create'
            return
        }

        someClassInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'someClass.label', default: 'SomeClass'), someClassInstance.id])
                redirect someClassInstance
            }
            '*' { respond someClassInstance, [status: CREATED] }
        }
    }

    def edit(SomeClass someClassInstance) {
        respond someClassInstance
    }

    @Transactional
    def update(SomeClass someClassInstance) {
        if (someClassInstance == null) {
            notFound()
            return
        }

        if (someClassInstance.hasErrors()) {
            respond someClassInstance.errors, view:'edit'
            return
        }

        someClassInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'SomeClass.label', default: 'SomeClass'), someClassInstance.id])
                redirect someClassInstance
            }
            '*'{ respond someClassInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(SomeClass someClassInstance) {

        if (someClassInstance == null) {
            notFound()
            return
        }

        someClassInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'SomeClass.label', default: 'SomeClass'), someClassInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'someClass.label', default: 'SomeClass'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

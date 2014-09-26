describe 'proximal', ->
  controller = undefined
  log = undefined

  beforeEach ->
    module 'proximal'

  describe 'LoginController', ->
    beforeEach(inject ($controller, $log)->
      controller = $controller
      log = $log
      controller = controller('LoginCtrl', {'$log', $log})
    )
    console.log(controller)
    it 'sets the name of the page', ->
      expect(controller.page).toBe('Login Page')


describe 'footer_directive', ->
  element = undefined
  scope = undefined

  beforeEach module 'proximal'

  beforeEach( inject ($rootScope, $compile)->
    scope = $rootScope.$new()
    element = '<prox-footer> </prox-footer>'

    element = $compile(element)(scope)
    scope.$digest()
  )

  it("should be a footer element", ->
    expect(element[0].tagName).toBe("FOOTER")
  )

  it("should have the copyright variable", ->
    isolated = element.isolateScope()
    expect(isolated.copyright).toBe(new Date().getFullYear())
  )



@file:Suppress("unused")

package org.kodein.di.generic

import org.kodein.di.Kodein
import org.kodein.di.bindings.*
import org.kodein.di.generic

/**
 * Used to define bindings with a scope: `bind<MyType>() with scoped(myScope).singleton { /*...*/ }`
 *
 * @param EC The scope's environment context type.
 * @param BC The scope's Binding context type.
 */
inline fun <reified C> Kodein.Builder.scoped(scope: Scope<C>): Kodein.BindBuilder.WithScope<C> = Kodein.BindBuilder.WithScope.Impl(generic(), scope)

/**
 * Used to define bindings with a context: `bind<MyType>() with contexted<MyContext>().provider { /*...*/ }`
 *
 * @param C The context type.
 */
inline fun <reified C> Kodein.Builder.contexted(): Kodein.BindBuilder.WithContext<C> = Kodein.BindBuilder.WithContext.Impl(generic())


/**
 * Creates a factory: each time an instance is needed, the function [creator] function will be called.
 *
 * A & T generics will be kept.
 *
 * @param A The argument type.
 * @param T The created type.
 * @param creator The function that will be called each time an instance is requested. Should create a new instance.
 * @return A factory ready to be bound.
 */
inline fun <C, reified A, reified T: Any> Kodein.BindBuilder.WithContext<C>.factory(noinline creator: BindingKodein<C>.(A) -> T) = Factory<C, A, T>(contextType, generic(), generic(), creator)

/**
 * Creates a factory: each time an instance is needed, the function [creator] function will be called.
 *
 * T generics will be kept.
 *
 * A provider is like a [factory], but without argument.
 *
 * @param T The created type.
 * @param creator The function that will be called each time an instance is requested. Should create a new instance.
 * @return A provider ready to be bound.
 */
inline fun <C, reified T: Any> Kodein.BindBuilder.WithContext<C>.provider(noinline creator: NoArgBindingKodein<C>.() -> T) = Provider(contextType, generic(), creator)

/**
 * Creates a singleton: will create an instance on first request and will subsequently always return the same instance.
 *
 * T generics will be kept.
 *
 * @param T The created type.
 * @param creator The function that will be called the first time an instance is requested. Guaranteed to be called only once. Should create a new instance.
 * @return A singleton ready to be bound.
 */
inline fun <C, reified T: Any> Kodein.BindBuilder.WithScope<C>.singleton(ref: RefMaker? = null, sync: Boolean = true, noinline creator: NoArgSimpleBindingKodein<C>.() -> T) = Singleton(scope, contextType, generic(), ref, sync, creator)

/**
 * Creates a multiton: will create an instance on first request for each different argument and will subsequently always return the same instance for the same argument.
 *
 * A & T generics will be kept.
 *
 * @param A The argument type.
 * @param T The created type.
 * @param creator The function that will be called the first time an instance is requested with a new argument. Guaranteed to be called only once per argument. Should create a new instance.
 * @return A multiton ready to be bound.
 */
inline fun <C, reified A, reified T: Any> Kodein.BindBuilder.WithScope<C>.multiton(ref: RefMaker? = null, sync: Boolean = true, noinline creator: SimpleBindingKodein<C>.(A) -> T) = Multiton<C, A, T>(scope, contextType, generic(), generic(), ref, sync, creator)

/**
 * Creates an eager singleton: will create an instance as soon as kodein is ready (all bindings are set) and will always return this instance.
 *
 * T generics will be kept.
 *
 * @param T The created type.
 * @param creator The function that will be called as soon as Kodein is ready. Guaranteed to be called only once. Should create a new instance.
 * @return An eager singleton ready to be bound.
 */
inline fun <reified T: Any> Kodein.Builder.eagerSingleton(noinline creator: NoArgSimpleBindingKodein<Any?>.() -> T) = EagerSingleton(containerBuilder, generic(), creator)

/**
 * Creates an instance provider: will always return the given instance.
 *
 * T generics will be kept.
 *
 * @param T The type of the instance.
 * @param instance The object that will always be returned.
 * @return An instance provider ready to be bound.
 */
inline fun <reified T: Any> Kodein.Builder.instance(instance: T) = InstanceBinding(generic(), instance)

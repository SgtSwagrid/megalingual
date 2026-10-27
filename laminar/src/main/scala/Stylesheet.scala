package com.alecdorrington.megalingual
package laminar

import org.scalajs.dom

/**
  * The picker's stylesheet, added to the page's head once. Every rule is in
  * `:where()`, so has no specificity, and any rule of the app's overrides it.
  */
private object Stylesheet:

  private val id = "megalingual-styles"

  /** Adds the stylesheet to the page, unless it is there already. */
  def install(): Unit = if dom.document.getElementById(id) == null then
    val style = dom.document.createElement("style")
    style.id = id
    style.textContent = css
    dom.document.head.prepend(style)

  private val border =
    "var(--megalingual-border, color-mix(in srgb, CanvasText 22%, transparent))"

  private val css = s"""
    |:where(.megalingual-picker) {
    |  position: relative;
    |  display: inline-block;
    |  max-width: 100%;
    |}
    |:where(.megalingual-button) {
    |  display: inline-flex;
    |  align-items: center;
    |  gap: 0.5em;
    |  max-width: 100%;
    |  padding: 0.375em 0.625em;
    |  font: inherit;
    |  color: var(--megalingual-text, CanvasText);
    |  background: var(--megalingual-surface, Canvas);
    |  border: 1px solid $border;
    |  border-radius: var(--megalingual-radius, 6px);
    |  cursor: pointer;
    |}
    |:where(.megalingual-name) {
    |  min-width: 0;
    |  overflow: hidden;
    |  text-overflow: ellipsis;
    |  white-space: nowrap;
    |}
    |:where(.megalingual-caret) {
    |  opacity: 0.7;
    |}
    |:where(.megalingual-hidden) {
    |  position: absolute;
    |  width: 1px;
    |  height: 1px;
    |  overflow: hidden;
    |  clip-path: inset(50%);
    |  white-space: nowrap;
    |}
    |:where(.megalingual-list) {
    |  position: absolute;
    |  top: calc(100% + 4px);
    |  inset-inline-start: 0;
    |  z-index: 20;
    |  box-sizing: border-box;
    |  width: max-content;
    |  min-width: max(100%, 12rem);
    |  max-width: calc(100vw - 2rem);
    |  max-height: min(26rem, calc(100vh - 5rem));
    |  margin: 0;
    |  padding: 0.375rem;
    |  overflow: hidden auto;
    |  overscroll-behavior: contain;
    |  list-style: none;
    |  color: var(--megalingual-text, CanvasText);
    |  background: var(--megalingual-surface, Canvas);
    |  border: 1px solid $border;
    |  border-radius: var(--megalingual-radius, 6px);
    |  box-shadow: var(--megalingual-shadow, 0 8px 24px rgb(0 0 0 / 0.16));
    |}
    |:where(.megalingual-list):focus {
    |  outline: none;
    |}
    |:where(.megalingual-option) {
    |  display: flex;
    |  align-items: center;
    |  gap: 0.5em;
    |  padding: 0.5em 0.625em;
    |  padding-inline-start: calc(0.625em + var(--megalingual-depth, 0) * 1.25em);
    |  font-size: 0.875em;
    |  border-radius: calc(var(--megalingual-radius, 6px) - 2px);
    |  cursor: pointer;
    |}
    |:where(.megalingual-option-active) {
    |  background: var(--megalingual-active, color-mix(in srgb, CanvasText 8%, transparent));
    |}
    |:where(.megalingual-option[aria-selected="true"]) {
    |  font-weight: 600;
    |  color: var(--megalingual-accent, LinkText);
    |}
    |:where(.megalingual-lead) {
    |  flex: 1 1 auto;
    |  min-width: 0;
    |  overflow: hidden;
    |  text-overflow: ellipsis;
    |  white-space: nowrap;
    |}
    |:where(.megalingual-aside) {
    |  flex: none;
    |  font-size: 0.85em;
    |  font-weight: 400;
    |  white-space: nowrap;
    |  opacity: 0.7;
    |}
    |:where(.megalingual-flag) {
    |  display: block;
    |  flex: none;
    |  width: 21px;
    |  height: 14px;
    |  border-radius: 2px;
    |  box-shadow: 0 0 0 1px rgb(16 24 40 / 0.15);
    |}
    |:where(.megalingual-flag-none) {
    |  box-shadow: none;
    |}
    |""".stripMargin

package com.alecdorrington.megalingual
package laminar

import org.scalajs.dom

/**
  * The picker's stylesheet, added once, first in the page's head. Each rule is
  * of one class, which outlasts element resets (`button`, `ul`), and comes
  * before any rule of the app's, which overrides it.
  */
private object Stylesheet:

  private val id = "megalingual-styles"

  /** Adds the stylesheet to the page, unless it is there already. */
  def install(): Unit = Option(dom.document.head)
    .filter(_ => dom.document.getElementById(id) == null)
    .foreach: head =>
      val style = dom.document.createElement("style")
      style.id = id
      style.textContent = css
      head.prepend(style)

  private val text = "var(--megalingual-text, CanvasText)"

  private val border =
    "var(--megalingual-border, color-mix(in srgb, CanvasText 22%, transparent))"

  private val accent = "var(--megalingual-accent, LinkText)"

  /**
    * The scrollbar's thumb, by default the text faded, so that it suits the
    * surface the app gives the popup, which the browser's own scrollbar, light
    * unless the page declares a dark `color-scheme`, does not. Chromium and
    * Safari draw it as a pill, with no arrows, by `::-webkit-scrollbar`, which
    * Chromium ignores wherever `scrollbar-color` is set, so that is set only
    * for Firefox, which lacks the pseudo-elements. The list resets it, which it
    * would otherwise inherit from a page that sets its own.
    */
  private val thumb =
    s"var(--megalingual-scrollbar, color-mix(in srgb, $text 35%, transparent))"

  private val css = s"""
    |.megalingual-picker {
    |  position: relative;
    |  display: inline-block;
    |  max-width: 100%;
    |}
    |.megalingual-button {
    |  display: inline-flex;
    |  align-items: center;
    |  gap: 0.5em;
    |  max-width: 100%;
    |  margin: 0;
    |  padding: 0.375em 0.625em;
    |  font: inherit;
    |  color: $text;
    |  background: var(--megalingual-surface, Canvas);
    |  border: 1px solid $border;
    |  border-radius: var(--megalingual-radius, 6px);
    |  cursor: pointer;
    |}
    |.megalingual-name {
    |  min-width: 0;
    |  overflow: hidden;
    |  text-overflow: ellipsis;
    |  white-space: nowrap;
    |}
    |.megalingual-caret {
    |  opacity: 0.7;
    |}
    |.megalingual-hidden {
    |  position: absolute;
    |  width: 1px;
    |  height: 1px;
    |  overflow: hidden;
    |  clip-path: inset(50%);
    |  white-space: nowrap;
    |}
    |.megalingual-popup {
    |  position: absolute;
    |  top: calc(100% + 4px);
    |  inset-inline-start: 0;
    |  z-index: 20;
    |  display: flex;
    |  flex-direction: column;
    |  box-sizing: border-box;
    |  width: max-content;
    |  min-width: max(100%, 12rem);
    |  max-width: calc(100vw - 2rem);
    |  max-height: min(26rem, calc(100vh - 5rem));
    |  overflow: hidden;
    |  color: $text;
    |  background: var(--megalingual-surface, Canvas);
    |  border: 1px solid $border;
    |  border-radius: var(--megalingual-radius, 6px);
    |  box-shadow: var(--megalingual-shadow, 0 8px 24px rgb(0 0 0 / 0.16));
    |}
    |.megalingual-search {
    |  display: flex;
    |  flex: none;
    |  align-items: center;
    |  gap: 0.5em;
    |  margin: 0.375rem 0.375rem 0;
    |  padding: 0.375em 0.625em;
    |  font-size: 0.875em;
    |  border: 1px solid $border;
    |  border-radius: calc(var(--megalingual-radius, 6px) - 2px);
    |  cursor: text;
    |}
    |.megalingual-search:focus-within {
    |  border-color: $accent;
    |}
    |.megalingual-search-icon {
    |  flex: none;
    |  width: 1em;
    |  height: 1em;
    |  opacity: 0.6;
    |}
    |.megalingual-input {
    |  flex: 1 1 0;
    |  width: 0;
    |  min-width: 0;
    |  margin: 0;
    |  padding: 0;
    |  font: inherit;
    |  color: inherit;
    |  background: none;
    |  border: none;
    |  border-radius: 0;
    |  outline: none;
    |  box-shadow: none;
    |}
    |.megalingual-input:focus {
    |  outline: none;
    |  box-shadow: none;
    |}
    |.megalingual-list {
    |  flex: 1 1 auto;
    |  min-height: 0;
    |  margin: 0;
    |  padding: 0.375rem;
    |  overflow: hidden auto;
    |  overscroll-behavior: contain;
    |  scrollbar-width: auto;
    |  scrollbar-color: auto;
    |  list-style: none;
    |}
    |.megalingual-list::-webkit-scrollbar {
    |  width: 10px;
    |}
    |.megalingual-list::-webkit-scrollbar-thumb {
    |  background: $thumb padding-box;
    |  border: 2px solid transparent;
    |  border-radius: 5px;
    |}
    |.megalingual-list::-webkit-scrollbar-thumb:hover {
    |  background-color: color-mix(in srgb, $thumb 70%, $text);
    |}
    |@supports not selector(::-webkit-scrollbar) {
    |  .megalingual-list {
    |    scrollbar-width: thin;
    |    scrollbar-color: $thumb transparent;
    |  }
    |}
    |.megalingual-option {
    |  display: flex;
    |  align-items: center;
    |  gap: 0.5em;
    |  margin: 0;
    |  padding: 0.5em 0.625em;
    |  padding-inline-start: calc(0.625em + var(--megalingual-depth, 0) * 1.25em);
    |  font-size: 0.875em;
    |  border-radius: calc(var(--megalingual-radius, 6px) - 2px);
    |  cursor: pointer;
    |}
    |.megalingual-option[aria-selected="true"] {
    |  font-weight: 600;
    |  color: $accent;
    |}
    |.megalingual-option-active {
    |  background: var(--megalingual-active, color-mix(in srgb, CanvasText 8%, transparent));
    |  outline: 2px solid $accent;
    |  outline-offset: -2px;
    |}
    |.megalingual-lead {
    |  flex: 1 1 auto;
    |  min-width: 0;
    |  overflow: hidden;
    |  text-overflow: ellipsis;
    |  white-space: nowrap;
    |}
    |.megalingual-aside {
    |  flex: none;
    |  font-size: 0.85em;
    |  font-weight: 400;
    |  white-space: nowrap;
    |  opacity: 0.7;
    |}
    |.megalingual-flag {
    |  display: block;
    |  flex: none;
    |  width: 21px;
    |  height: 14px;
    |  border-radius: 2px;
    |  box-shadow: 0 0 0 1px rgb(16 24 40 / 0.15);
    |}
    |.megalingual-flag-none {
    |  box-shadow: none;
    |}
    |@media (forced-colors: active) {
    |  .megalingual-option-active {
    |    outline-color: Highlight;
    |  }
    |  .megalingual-search:focus-within {
    |    border-color: Highlight;
    |  }
    |}
    |""".stripMargin

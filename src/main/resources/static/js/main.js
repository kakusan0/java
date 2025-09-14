$(function () {
  const on = (selector, evt, handler, ns = '') => {
    const evtName = ns ? `${evt}.${ns}` : evt;
    $(selector).off(evtName).on(evtName, handler);
  };

  on('#pcSidebarToggle', 'click', () => $('#sidebarMenu, .main-content').toggleClass('is-collapsed'));

  const $sidebar = $('#sidebarMenu');
  if ($sidebar.length) bootstrap.Offcanvas.getOrCreateInstance($sidebar[0]);

  const setAppHeight = () => $('html').css('--app-height', `${window.innerHeight}px`);
  on(window, 'resize', setAppHeight, 'app');
  setAppHeight();

  on('#liveToastBtn', 'click', () => bootstrap.Toast.getOrCreateInstance($('#liveToast')[0]).show(), 'toast');

  on('#errorModal', 'shown.bs.modal', () => $('.modal-backdrop').last().addClass('backdrop-error'), 'modal');
  on('#scrollableModal', 'shown.bs.modal', () => $('.modal-backdrop').last().addClass('backdrop-select'), 'modal');
});

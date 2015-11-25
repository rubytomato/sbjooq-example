package com.example.jooq.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jooq.db.tables.records.ActorRecord;
import com.example.jooq.db.tables.records.PrefectureRecord;
import com.example.jooq.service.ActorService;
import com.example.jooq.service.PrefectureService;
import com.example.jooq.utils.DateParser;

@Controller
@RequestMapping(value = "/actor")
public class ActorController {
  final static Logger logger = LoggerFactory.getLogger(ActorController.class);

  @Autowired
  ActorService actorService;

  @Autowired
  PrefectureService prefectureService;

  @Autowired
  MessageSource msg;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    sdf.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
  }

  @RequestMapping(method = RequestMethod.GET)
  public String _index(Model model) {
    return index(model);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index(Model model) {
    logger.debug("Actor + index");
    Result<ActorRecord> result = actorService.findAll();
    if (CollectionUtils.isEmpty(result)) {
      String message = msg.getMessage("actor.list.empty", null, Locale.JAPAN);
      model.addAttribute("emptyMessage", message);
    }
    model.addAttribute("result", result);
    return "Actor/index";
  }

  @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
  public String detail(@PathVariable Integer id, Model model) {
    logger.debug("Actor + detail");
    ActorRecord actor = actorService.findOne(id);
    model.addAttribute("actor", actor);
    return "Actor/detail";
  }

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public String search(@RequestParam String keyword, Model model) {
    logger.debug("Actor + search");
    if (StringUtils.isNotEmpty(keyword)) {
      Result<ActorRecord> result = actorService.findAll();
      if (CollectionUtils.isEmpty(result)) {
        String message = msg.getMessage("actor.list.empty", null, Locale.JAPAN);
        model.addAttribute("emptyMessage", message);
      }
      model.addAttribute("result", result);
    }
    return "Actor/index";
  }

  @RequestMapping(value = "/create", method = RequestMethod.GET)
  public String create(ActorForm form, Model model) {
    logger.debug("Actor + create");
    Result<PrefectureRecord> pref =  prefectureService.findAll();
    model.addAttribute("pref", pref);
    return "Actor/create";
  }

  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public String save(@Validated @ModelAttribute ActorForm form, BindingResult result, Model model) {
    logger.debug("Actor + save");
    if (result.hasErrors()) {
      String message = msg.getMessage("actor.validation.error", null, Locale.JAPAN);
      model.addAttribute("errorMessage", message);
      return create(form, model);
    }
    ActorRecord actor = convert(form);
    logger.debug("actor:{}", actor.toString());
    actor = actorService.insert(actor);
    return "redirect:/actor/detail/" + actor.getId().toString();
  }

  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
    logger.debug("Actor + delete");
    actorService.delete(id);
    attributes.addFlashAttribute("deleteMessage", "delete ID:" + id);
    return "redirect:/actor/";
  }

  private ActorRecord convert(ActorForm form) {
    ActorRecord actor = new ActorRecord();
    actor.setName(form.getName());
    if (StringUtils.isNotEmpty(form.getHeight())) {
      actor.setHeight(Short.valueOf(form.getHeight()));
    }
    if (StringUtils.isNotEmpty(form.getBlood())) {
      actor.setBlood(form.getBlood());
    }
    if (StringUtils.isNotEmpty(form.getBirthday())) {
      DateTimeFormatter withoutZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime parsed = LocalDateTime.parse(form.getBirthday() + " 00:00:00", withoutZone);
      actor.setBirthday(DateParser.parseSQL(parsed));
    }
    if (StringUtils.isNotEmpty(form.getBirthplaceId())) {
      actor.setBirthplaceId(Short.valueOf(form.getBirthplaceId()));
    }
    actor.setUpdateAt(new Timestamp(new Date().getTime()));
    return actor;
  }

}
